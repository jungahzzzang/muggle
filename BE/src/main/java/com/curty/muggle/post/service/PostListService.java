package com.curty.muggle.post.service;

import com.curty.muggle.common.component.PostValidator;
import com.curty.muggle.post.dto.response.PostResponse;
import com.curty.muggle.post.entity.PostCategory;
import com.curty.muggle.post.entity.PostSortCondition;
import com.curty.muggle.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostListService {
    private static final int POST_PAGE_SIZE = 10;
    private final PostRepository postRepository;
    private final PostValidator postValidator;

    /**
     * 입력된 문자열 정렬 조건을 {@link PostSortCondition} Enum으로 변환하는 내부 메서드
     * 변환 전에 {@link PostValidator}를 통해 유효성을 검사합니다.
     *
     * @param sort 클라이언트로부터 받은 정렬 조건 문자열
     * @return 변환된 {@link PostSortCondition} Enum 값
     */
    private PostSortCondition convertSortConditionIntoEnumClass(String sort) {
        postValidator.checkSortCondition(sort);

        return PostSortCondition.valueOf(sort);
    }

    /**
     * 입력된 문자열 카테고리 조건을 {@link PostCategory} Enum으로 변환하는 내부 메서드
     * 변환 전에 {@link PostValidator}를 통해 유효성을 검사합니다.
     *
     * @param category 클라이언트로부터 받은 카테고리 문자열
     * @return 변환된 {@link PostCategory} Enum 값
     */
    private PostCategory convertPostCategoryIntoEnumClass(String category) {
        if (category == null || category.isEmpty()) return null;
        else {
            postValidator.checkCategory(category);

            return PostCategory.valueOf(category);
        }
    }

    /**
     * 페이징, 정렬, 카테고리, 키워드 검색 조건을 기반으로 게시글 목록을 조회하는 메서드
     *
     * @param page          현재 페이지 번호
     * @param inputSort     클라이언트로부터 받은 정렬 조건 문자열
     * @param inputCategory 클라이언트로부터 받은 카테고리 문자열
     * @param keyword       검색할 키워드 (제목 또는 내용에서 검색)
     * @return {@link PostResponse} 객체를 포함하는 {@link Page} 객체
     */
    @Transactional(readOnly = true)
    public Page<PostResponse> getAll(int page, String inputSort, String inputCategory, String keyword) {
        // enum 값으로 변환
        PostSortCondition sort = convertSortConditionIntoEnumClass(inputSort);
        PostCategory category = convertPostCategoryIntoEnumClass(inputCategory);

        // 페이지 객체 생성
        Pageable pageable = PageRequest.of(page - 1, POST_PAGE_SIZE);

        // QueryDsl를 활용한 게시글 목록 조회
        return postRepository.getAll(pageable, sort, category, keyword);
    }
}
