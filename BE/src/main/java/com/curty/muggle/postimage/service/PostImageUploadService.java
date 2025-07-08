package com.curty.muggle.postimage.service;

import com.curty.muggle.common.component.GcpImageUploader;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.postimage.entity.PostImage;
import com.curty.muggle.postimage.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostImageUploadService {
    private final GcpImageUploader gcpImageUploader;
    private final PostImageRepository postImageRepository;

    /**
     * 여러 이미지 파일을 Google Cloud Storage에 업로드하고
     * 성공적으로 업로드된 파일들의 메타데이터를 데이터베이스에 저장하는 메서드로,
     * 모든 파일에 대한 유효성 검사를 선행하며, GCS 업로드 중 하나라도 실패하면
     * 데이터베이스 저장은 수행되지 않는다.
     *
     * @param post   업로드할 파일들이 연결될 게시글 엔티티
     * @param images 업로드할 MultipartFile 목록
     */
    public void upload(Post post, List<MultipartFile> images) {
        // 모든 파일에 대한 검증을 우선적으로 진행
        for (MultipartFile image : images) {
            gcpImageUploader.validate(image);
        }

        List<PostImage> postImages = new ArrayList<>();

        // GCS에 이미지 파일들을 업로드
        for (MultipartFile image : images) {
            UUID postImageId = UUID.randomUUID();
            String imageUrl = gcpImageUploader.uploadImage("post", post.getPostId(), postImageId, image);

            // 이미지 업로드 완료 시 PostImage 객체를 생성
            PostImage postImage = PostImage.builder()
                    .postImageId(postImageId)
                    .post(post)
                    .originalImageName(image.getOriginalFilename())
                    .imageUrl(imageUrl)
                    .imageSize(image.getSize())
                    .imageType(image.getContentType())
                    .build();

            postImages.add(postImage);
        }

        // 모든 이미지가 GCS에 업로드되어야만 PostImage 리스트를 DB에 저장
        postImageRepository.saveAll(postImages);
    }
}
