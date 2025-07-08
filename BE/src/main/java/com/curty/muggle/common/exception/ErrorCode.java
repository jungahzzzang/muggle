package com.curty.muggle.common.exception;

public enum ErrorCode {
    // 멤버 관련
    MEMBER_NOT_FOUND,                 // 특정 id에 대한 유저가 존재하지 않는 경우
    
    // 게시글 관련
    POST_NOT_FOUND,                   // 특정 id에 대한 게시글이 존재하지 않는 경우
    POST_TITLE_EMPTY,                 // 게시글 제목을 입력하지 않은 경우
    POST_TITLE_EXCEEDS_MAX_LENGTH,    // 게시글 제목의 글자수가 255자를 초과한 경우
    POST_CONTENT_EMPTY,               // 게시글 내용을 입력하지 않은 경우
    POST_CATEGORY_EMPTY,              // 게시글 카테고리를 입력하지 않은 경우
    POST_CATEGORY_INVALID,            // 게시글 카테고리가 올바르지 않은 경우
    POST_SORT_INVALID,                // 게시글 정렬 조건이 올바르지 않은 경우
    
    CANNOT_UPDATE_OTHERS_POST,        // 다른 유저의 게시글을 수정하려는 경우
    CANNOT_ACCESS_DELETED_POST,       // 삭제된 게시글에 접근하는 경우
    
    // 이미지 관련
    NO_IMAGE_FILE,                    // 이미지 파일이 존재하지 않는 경우
    INVALID_IMAGE_FILE,               // 이미지 파일이 유효하지 않은 경우
    INVALID_IMAGE_EXTENSION,          // 이미지 파일 확장자가 유효하지 않은 경우
    IMAGE_EXCEEDS_MAX_SIZE,           // 이미지 파일의 크기가 10MB를 초과한 경우
    GCP_STORAGE_ERROR,                // GCP 스토리지 관련 에러가 발생한 경우
}
