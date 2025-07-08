package com.curty.muggle.common.exception.custom;

import com.curty.muggle.common.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InvalidEnumValueException extends RuntimeException {
    private final String message;
    private final int statusCode;
    private final ErrorCode errorCode;

    public InvalidEnumValueException(ErrorCode errorCode) {
        switch (errorCode) {
            case POST_CATEGORY_INVALID -> this.message = "존재하지 않는 카테고리입니다.";
            case POST_SORT_INVALID ->this.message = "존재하지 않는 정렬 조건입니다.";
            default -> this.message = "알 수 없는 오류가 발생했습니다.";
        }
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.errorCode = errorCode;
    }
}
