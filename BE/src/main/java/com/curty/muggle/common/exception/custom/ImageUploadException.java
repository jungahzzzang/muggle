package com.curty.muggle.common.exception.custom;

import com.curty.muggle.common.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ImageUploadException extends RuntimeException {
    private final String message;
    private final int statusCode;
    private final ErrorCode errorCode;

    public ImageUploadException(ErrorCode errorCode) {
        switch (errorCode) {
            case ErrorCode.NO_IMAGE_FILE -> this.message = "이미지 파일이 존재하지 않습니다.";
            case ErrorCode.INVALID_IMAGE_FILE -> this.message = "유효하지 않은 이미지 파일입니다.";
            case ErrorCode.INVALID_IMAGE_EXTENSION -> this.message = "유효하지 않은 이미지 확장자입니다.";
            case ErrorCode.IMAGE_EXCEEDS_MAX_SIZE -> this.message = "최대 10MB의 이미지만 업로드 가능합니다.";
            default -> this.message = "알 수 없는 오류가 발생했습니다.";
        }
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.errorCode = errorCode;
    }
}
