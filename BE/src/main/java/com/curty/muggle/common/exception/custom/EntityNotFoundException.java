package com.curty.muggle.common.exception.custom;

import com.curty.muggle.common.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EntityNotFoundException extends RuntimeException {
    private final String message;
    private final int statusCode;
    private final ErrorCode errorCode;

    public EntityNotFoundException(ErrorCode errorCode) {
        switch (errorCode) {
            case MEMBER_NOT_FOUND -> this.message = "존재하지 않는 유저입니다.";
            case POST_NOT_FOUND -> this.message = "존재하지 않는 게시글입니다.";
            default -> this.message = "알 수 없는 에러가 발생했습니다.";
        }
        this.statusCode = HttpStatus.NOT_FOUND.value();
        this.errorCode = errorCode;
    }
}