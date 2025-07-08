package com.curty.muggle.common.component;

import com.curty.muggle.common.exception.ErrorCode;
import com.curty.muggle.common.exception.custom.ImageUploadException;
import com.curty.muggle.common.exception.custom.InternalSystemException;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class GcpImageUploader {
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final Storage storage;

    private static final String IMAGE_NAME_PATTERN = "%s-%s-%s.%s";
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg"
    );

    /**
     * 원본 파일명에서 확장자를 추출하는 내부 메서드
     * 파일명에 '.'이 없거나 '.'이 마지막에 위치하는 경우, 유효하지 않은 파일명으로 간주하고 예외를 발생
     *
     * @param originalImageName 원본 파일명 (예: "image.jpg")
     * @return 파일의 확장자 (예: "jpg")
     * @throws ImageUploadException 파일명에서 확장자를 추출할 수 없거나 형식이 유효하지 않을 경우
     */
    private String getExtension(String originalImageName) {
        int lastDotIndex = originalImageName.lastIndexOf(".");

        if (lastDotIndex == -1 || lastDotIndex == originalImageName.length() - 1) {
            throw new ImageUploadException(ErrorCode.INVALID_IMAGE_FILE);
        }

        return originalImageName.substring(lastDotIndex + 1);
    }

    /**
     * 업로드할 MultipartFile의 유효성을 검사하는 메서드
     * 파일이 null이거나 비어있는지, 파일명에 확장자가 유효하게 포함되어 있는지,
     * 그리고 해당 확장자가 허용된 이미지 확장자 목록에 포함되는지 확인
     *
     * @param image 유효성을 검사할 MultipartFile 객체
     * @throws ImageUploadException 파일이 null이거나 비어있을 경우, 혹은 확장자가 올바르지 않은 경우
     */
    public void validate(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ImageUploadException(ErrorCode.NO_IMAGE_FILE);
        }

        if (image.getSize() > 10 * 1024 * 1024) { // 10MB
            throw new ImageUploadException(ErrorCode.IMAGE_EXCEEDS_MAX_SIZE);
        }

        String originalImageName = image.getOriginalFilename();
        if (originalImageName == null || !originalImageName.contains(".")) {
            throw new ImageUploadException(ErrorCode.INVALID_IMAGE_FILE);
        }

        String extension = getExtension(originalImageName);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new ImageUploadException(ErrorCode.INVALID_IMAGE_EXTENSION);
        }
    }

    /**
     * 단일 이미지를 Google Cloud Storage에 업로드하는 메서드
     * 파일명은 {@code identifier-primaryKey-imageId.extension} 패턴으로 생성
     *
     * @param identifier 게시글 식별자 또는 이미지 종류를 나타내는 문자열
     * @param primaryKey 게시글의 기본 키 (ID)
     * @param imageId    업로드할 이미지에 부여할 고유 UUID
     * @param image       업로드할 MultipartFile 객체
     * @return 업로드된 이미지의 공개 접근 URL (예: {@code https://storage.googleapis.com/your-bucket/image-filename.jpg})
     * @throws InternalSystemException GCS 업로드 과정에서 알 수 없는 에러가 발생한 경우
     * @throws ImageUploadException 파일 유효성 검사에 실패한 경우
     */
    public String uploadImage(String identifier, Long primaryKey, UUID imageId, MultipartFile image) {
        // 확장자 추출
        String originalImageName = image.getOriginalFilename();
        assert originalImageName != null;
        String extension = getExtension(originalImageName);

        // 파일명 생성
        String imageName = IMAGE_NAME_PATTERN.formatted(identifier, primaryKey, imageId, extension);

        // 이미지 타입
        String contentType = image.getContentType();

        // 이미지 정보(메타 데이터) 설정
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, imageName)
                .setContentType(contentType)
                .build();

        // GCP Storage에 이미지 업로드
        try {
            try (
                    WriteChannel writer = storage.writer(blobInfo);
                    InputStream inputStream = image.getInputStream()
            ) {
                inputStream.transferTo(Channels.newOutputStream(writer));
                log.info("GCP Storage 업로드 성공: {}", imageName);
            }

            return String.format("https://storage.googleapis.com/%s/%s", bucketName, imageName);
        } catch (Exception e) {
            log.error("GCP Storage 업로드 과정에서 에러가 발생했습니다. 파일: {}", originalImageName, e);
            throw new InternalSystemException(ErrorCode.GCP_STORAGE_ERROR);
        }
    }
}
