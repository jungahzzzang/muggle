package com.curty.muggle.post.service;

import com.curty.muggle.post.dto.response.PostFormResponse;
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
public class PostGetUpdateFormService {
    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final Executor taskExecutor;

    public PostGetUpdateFormService(
            PostImageRepository postImageRepository,
            PostRepository postRepository,
            @Qualifier("threadPoolTaskExecutor") Executor taskExecutor
    ) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
        this.taskExecutor = taskExecutor;
    }

    /**
     * 특정 ID를 가진 게시글의 상세 정보를 조회하는 메서드 (수정 폼에 입력하기 위한)
     *
     * @param postId 조회할 게시글의 고유 ID
     * @return 조회된 {@link PostFormResponse} 객체
     */
    @Transactional(readOnly = true)
    public PostFormResponse getFormInfo(Long postId) {
        // 게시글 기본 정보 조회 (비동기)
        CompletableFuture<PostFormResponse> postFuture =
                CompletableFuture.supplyAsync(() -> postRepository.getFormInfo(postId), taskExecutor);

        // 이미지 목록 조회 (비동기)
        CompletableFuture<List<PostImageResponse>> imagesFuture =
                CompletableFuture.supplyAsync(() -> postImageRepository.getAll(postId), taskExecutor);

        // 결과 조합 + 검증
        PostFormResponse postFormResponse = postFuture.join();
        List<PostImageResponse> postImageResponse = imagesFuture.join();

        postFormResponse.setImages(postImageResponse);

        // 리턴
        return postFormResponse;
    }
}
