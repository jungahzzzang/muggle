package com.curty.muggle.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponse {
    private String message;         // 에러 메시지
    private int statusCode;         // HTTP 상태 코드
    private ErrorCode errorCode;    // 에러 코드
}
