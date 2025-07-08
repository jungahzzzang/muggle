package com.curty.muggle.common.component;

import com.curty.muggle.common.exception.ErrorCode;
import com.curty.muggle.common.exception.custom.InvalidEnumValueException;
import com.curty.muggle.common.exception.custom.ValidationException;
import com.curty.muggle.post.entity.PostCategory;
import com.curty.muggle.post.entity.PostSortCondition;
import org.springframework.stereotype.Component;

@Component
public class PostValidator {
    private final static int POST_TITLE_MAX_LENGTH = 255;

    /**
     * 게시글 제목의 유효성을 검사하는 메서드
     * 제목이 null이거나 비어있는지, 최대 길이를 초과하는지 확인
     *
     * @param title 검사할 게시글 제목
     * @throws ValidationException 제목이 유효하지 않을 경우 (빈 값, 길이 초과)
     */
    public void checkTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ValidationException(ErrorCode.POST_TITLE_EMPTY);
        }
        if (title.length() > POST_TITLE_MAX_LENGTH) {
            throw new ValidationException(ErrorCode.POST_TITLE_EXCEEDS_MAX_LENGTH);
        }
    }

    /**
     * 게시글 내용의 유효성을 검사하는 메서드
     * 내용이 null이거나 비어있는지 확인
     *
     * @param content 검사할 게시글 내용
     * @throws ValidationException 내용이 유효하지 않을 경우 (빈 값)
     */
    public void checkContent(String content) {
        if (content == null || content.isBlank()) {
            throw new ValidationException(ErrorCode.POST_CONTENT_EMPTY);
        }
    }

    /**
     * 게시글 카테고리 문자열의 유효성을 검사하여 {@link PostCategory} Enum으로 변환 가능한지 확인하는 메서드
     *
     * @param category 검사할 카테고리 문자열
     * @throws ValidationException 유효하지 않은 카테고리 문자열일 경우
     */
    public void checkCategory(String category) {
        try {
            PostCategory.valueOf(category);
        } catch (Exception e) {
            throw new ValidationException(ErrorCode.POST_CATEGORY_INVALID);
        }
    }

    /**
     * 게시글 정렬 조건 문자열의 유효성을 검사하여 {@link PostSortCondition} Enum으로 변환 가능한지 확인하는 메서드
     *
     * @param sort 검사할 정렬 조건 문자열
     * @throws InvalidEnumValueException 유효하지 않은 정렬 조건 문자열일 경우
     */
    public void checkSortCondition(String sort) {
        try {
            PostSortCondition.valueOf(sort);
        } catch (Exception e) {
            throw new InvalidEnumValueException(ErrorCode.POST_SORT_INVALID);
        }
    }
}
