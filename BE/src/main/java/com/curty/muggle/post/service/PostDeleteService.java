package com.curty.muggle.post.service;

import com.curty.muggle.common.component.EntityFinder;
import com.curty.muggle.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostDeleteService {
    private final EntityFinder entityFinder;

    /**
     * 지정된 ID의 게시글을 소프트 삭제 처리하는 메서드
     *
     * @param postId 삭제할 게시글의 ID
     * @param userId 삭제를 요청한 사용자의 ID
     */
    @Transactional
    public void delete(Long postId, Long userId) {
        // Post 객체 조회
        Post post = entityFinder.getPost(postId);

        // soft delete
        post.delete();
    }
}
