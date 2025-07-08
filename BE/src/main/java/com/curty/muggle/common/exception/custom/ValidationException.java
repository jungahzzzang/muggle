package com.curty.muggle.common.exception.custom;

import com.curty.muggle.common.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ValidationException extends RuntimeException{
    private final String message;
    private final int statusCode;
    private final ErrorCode errorCode;

    public ValidationException(ErrorCode errorCode) {
        switch (errorCode) {
            case ErrorCode.POST_TITLE_EMPTY -> this.message = "제목은 필수 입력 항목입니다.";
            case ErrorCode.POST_TITLE_EXCEEDS_MAX_LENGTH -> this.message = "제목은 최대 255자까지 가능합니다.";
            case ErrorCode.POST_CONTENT_EMPTY -> this.message = "내용은 필수 입력 항목입니다.";
            case ErrorCode.POST_CATEGORY_EMPTY -> this.message = "카테고리는 필수 입력 항목입니다.";
            case ErrorCode.POST_CATEGORY_INVALID -> this.message = "존재하지 않는 카테고리입니다.";
            default -> this.message = "알 수 없는 오류가 발생했습니다.";
        }
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.errorCode = errorCode;
    }
}
