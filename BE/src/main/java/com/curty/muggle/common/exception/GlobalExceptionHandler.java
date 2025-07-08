package com.curty.muggle.common.exception;

import com.curty.muggle.common.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 올바르지 않은 enum 값이 입력값으로 들어온 경우
    @ExceptionHandler(InvalidEnumValueException.class)
    ResponseEntity<ErrorResponse> handleInvalidEnumValueException(InvalidEnumValueException e) {
        ErrorResponse error = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 특정 ID에 대한 엔티티 객체를 찾을 수 없는 경우
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorResponse error = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 사용자 입력값이 올바르지 않은 경우
    @ExceptionHandler(ValidationException.class)
    ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse error = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 접근할 수 없는 게시글에 접근하고자 하는 경우
    @ExceptionHandler(PostAccessException.class)
    ResponseEntity<ErrorResponse> handlePostAccessException(PostAccessException e) {
        ErrorResponse error = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 입력받은 이미지가 올바르지 않은 경우
    @ExceptionHandler(ImageUploadException.class)
    ResponseEntity<ErrorResponse> handleImageUploadException(ImageUploadException e) {
        ErrorResponse error = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 외부 API와 관련된 에러가 발생한 경우
    @ExceptionHandler(InternalSystemException.class)
    ResponseEntity<ErrorResponse> handleInternalSystemException(InternalSystemException e) {
        ErrorResponse error = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
