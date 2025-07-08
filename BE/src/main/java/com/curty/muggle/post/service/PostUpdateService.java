package com.curty.muggle.post.service;

import com.curty.muggle.common.component.EntityFinder;
import com.curty.muggle.common.component.PostValidator;
import com.curty.muggle.post.dto.request.PostUpdateRequest;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.postimage.service.PostImageDeleteService;
import com.curty.muggle.postimage.service.PostImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostUpdateService {
    private final EntityFinder entityFinder;
    private final PostImageDeleteService postImageDeleteService;
    private final PostImageUploadService postImageUploadService;
    private final PostValidator postValidator;

    /**
     * 게시글 업데이트 요청 DTO의 필드 유효성을 검사하는 내부 메서드
     * 제목, 내용, 카테고리의 유효성을 {@link PostValidator}를 통해 확인
     *
     * @param request 업데이트 요청 DTO ({@link PostUpdateRequest})
     */
    private void validate(PostUpdateRequest request) {
        postValidator.checkTitle(request.getTitle());
        postValidator.checkContent(request.getContent());
        postValidator.checkCategory(request.getCategory());
    }

    /**
     * 지정된 ID의 게시글 정보를 업데이트하는 메서드
     * 변경 감지(Dirty Checking)를 통해 데이터베이스에 반영
     * 삭제할 이미지들은 DB에서만 삭제하고, (GCP에는 이미지 보존)
     * 새로 추가된 이미지들은 GCP에 업로드하고 DB에 저장
     *
     * @param postId  업데이트할 게시글의 ID
     * @param request 업데이트할 내용을 담은 DTO ({@link PostUpdateRequest})
     * @param memberId  업데이트를 요청한 사용자의 ID
     */
    @Transactional
    public void update(Long postId, PostUpdateRequest request, Long memberId) {
        // 1차 검증 (입력값 검증)
        validate(request);

        // Post 객체 조회
        Post post = entityFinder.getPost(postId);

        // 업데이트
        post.update(request);

        // '삭제할 이미지' 삭제
        if (request.getImagesToDelete() != null && !request.getImagesToDelete().isEmpty()) {
            postImageDeleteService.delete(request.getImagesToDelete());
        }

        // '새로운 이미지' 업로드 및 저장
        if (request.getNewImages() != null && !request.getNewImages().isEmpty()) {
            postImageUploadService.upload(post, request.getNewImages());
        }
    }
}
