package com.curty.muggle.postlike.service;

import com.curty.muggle.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeCheckService {
    private final PostLikeRepository postLikeRepository;

    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 여부를 확인하는 메서드
     *
     * @param memberId 좋아요 여부를 확인할 사용자의 고유 ID
     * @param postId   좋아요 여부를 확인할 게시글의 고유 ID
     * @return 해당 사용자가 게시글에 좋아요를 눌렀으면 {@code true}, 그렇지 않으면 {@code false}
     */
    @Transactional(readOnly = true)
    public boolean check(Long memberId, Long postId) {
        return postLikeRepository.hasLike(memberId, postId) > 0;
    }
}
