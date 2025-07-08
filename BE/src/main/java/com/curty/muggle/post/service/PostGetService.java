package com.curty.muggle.post.service;

import com.curty.muggle.common.exception.ErrorCode;
import com.curty.muggle.common.exception.custom.EntityNotFoundException;
import com.curty.muggle.post.dto.response.PostDetailResponse;
import com.curty.muggle.post.entity.PostState;
import com.curty.muggle.post.repository.PostRepository;
import com.curty.muggle.postimage.dto.response.PostImageResponse;
import com.curty.muggle.postimage.repository.PostImageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class PostGetService {
    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final PostViewCountService postViewCountService;
    private final Executor taskExecutor;

    public PostGetService(
            PostImageRepository postImageRepository,
            PostRepository postRepository,
            PostViewCountService postViewCountService,
            @Qualifier("threadPoolTaskExecutor") Executor taskExecutor
    ) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
        this.postViewCountService = postViewCountService;
        this.taskExecutor = taskExecutor;
    }

    /**
     * 조회된 게시글의 유효성을 검사하는 내부 메서드
     * 게시글이 존재하지 않거나, 삭제된 상태일 경우 {@link EntityNotFoundException}을 발생
     *
     * @param post 유효성을 검사할 {@link PostDetailResponse} 객체
     * @throws EntityNotFoundException 게시글을 찾을 수 없거나 삭제된 게시글에 접근하려는 경우
     */
    private void validate(PostDetailResponse post) {
        if (post == null) {
            throw new EntityNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        if (post.getState().equals(PostState.DELETED)) {
            throw new EntityNotFoundException(ErrorCode.CANNOT_ACCESS_DELETED_POST);
        }
    }

    /**
     * 특정 ID를 가진 게시글의 상세 정보를 조회하고, 조건에 따라 조회수를 증가시키는 메서드
     * 조회수 증가는 별도의 비동기 트랜잭션으로 처리
     *
     * @param postId                   조회할 게시글의 고유 ID
     * @param shouldIncrementViewCount 조회수를 증가시킬지 여부를 나타내는 플래그
     * @return 조회된 {@link PostDetailResponse} 객체
     * @throws EntityNotFoundException 게시글을 찾을 수 없거나 삭제된 게시글에 접근하려는 경우
     */
    @Transactional
    public PostDetailResponse get(Long postId, boolean shouldIncrementViewCount) {
        // 게시글 기본 정보 조회 (비동기)
        CompletableFuture<PostDetailResponse> postFuture =
                CompletableFuture.supplyAsync(() -> postRepository.get(postId), taskExecutor);

        // 이미지 목록 조회 (비동기)
        CompletableFuture<List<PostImageResponse>> imagesFuture =
                CompletableFuture.supplyAsync(() -> postImageRepository.getAll(postId), taskExecutor);

        // 결과 조합 + 검증
        PostDetailResponse postDetailResponse = postFuture.join();
        List<PostImageResponse> postImageResponse = imagesFuture.join();

        validate(postDetailResponse);
        postDetailResponse.setImages(postImageResponse);

        // 조회수 증가 로직은 별도의 비동기 서비스 메서드로 분리
        if (shouldIncrementViewCount) {
            postViewCountService.incrementViewCount(postId);
        }

        // 리턴
        return postDetailResponse;
    }
}
