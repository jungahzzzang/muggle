package com.curty.muggle.common.exception.custom;

import com.curty.muggle.common.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PostAccessException extends RuntimeException {
    private final String message;
    private final int statusCode;
    private final ErrorCode errorCode;

    public PostAccessException(ErrorCode errorCode) {
        switch (errorCode) {
            case ErrorCode.CANNOT_UPDATE_OTHERS_POST -> this.message = "다른 유저의 게시글을 제어할 수 없습니다.";
            case ErrorCode.CANNOT_ACCESS_DELETED_POST -> this.message = "삭제된 게시글에 접근할 수 없습니다";
            default -> this.message = "알 수 없는 오류가 발생했습니다.";
        }
        this.statusCode = HttpStatus.UNAUTHORIZED.value();
        this.errorCode = errorCode;
    }
}
