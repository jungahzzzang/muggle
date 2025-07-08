package com.curty.muggle.post.service;

import com.curty.muggle.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostViewCountService {
    private final PostRepository postRepository;

    /**
     * 게시글 조회수를 비동기적으로 증가시키는 메서드
     * 이 메서드는 별도의 트랜잭션으로 '비동기적으로' 실행
     *
     * @param postId 조회수를 증가시킬 게시글의 고유 ID
     */
    @Async("threadPoolTaskExecutor")
    @Transactional
    public void incrementViewCount(Long postId) {
        postRepository.incrementViewCount(postId);
    }
}
