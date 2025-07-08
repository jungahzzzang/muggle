package com.curty.muggle.common.exception.custom;

import com.curty.muggle.common.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InternalSystemException extends RuntimeException {
    private final String message;
    private final int statusCode;
    private final ErrorCode errorCode;

    public InternalSystemException(ErrorCode errorCode) {
        switch (errorCode) {
            case ErrorCode.GCP_STORAGE_ERROR -> this.message = "GCP 스토리지에 이미지 업로드 중 에러가 발생했습니다.";
            default -> this.message = "알 수 없는 오류가 발생했습니다.";
        }
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.errorCode = errorCode;
    }
}
