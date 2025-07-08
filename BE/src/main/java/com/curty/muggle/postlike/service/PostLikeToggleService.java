package com.curty.muggle.postlike.service;

import com.curty.muggle.common.component.EntityFinder;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.postlike.entity.PostLike;
import com.curty.muggle.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeToggleService {
    private final EntityFinder entityFinder;
    private final PostLikeRepository postLikeRepository;

    /**
     * 특정 회원과 게시글에 대한 좋아요 기록을 저장하는 내부 메서드
     * {@code PostLike} 엔티티를 생성하고 데이터베이스에 저장
     *
     * @param member 좋아요를 누른 회원 엔티티
     * @param post   좋아요를 받은 게시글 엔티티
     */
    private void save(Member member, Post post) {
        PostLike postLike = PostLike.builder()
                .member(member)
                .post(post)
                .build();

        postLikeRepository.save(postLike);
    }

    /**
     * 특정 회원과 게시글에 대한 좋아요 기록을 삭제하는 메서드
     * 데이터베이스에서 해당 좋아요 기록을 찾아 삭제합니다.
     *
     * @param member 좋아요를 취소할 회원 엔티티
     * @param post   좋아요를 취소할 게시글 엔티티
     */
    private void delete(Member member, Post post) {
        postLikeRepository.deleteByMemberAndPost(member, post);
    }

    /**
     * 특정 게시글에 대한 좋아요 상태를 토글(추가 또는 삭제)하는 메서드
     * {@code hasLike} 파라미터 값에 따라 좋아요를 추가하거나 취소하며,
     * 이에 맞춰 해당 게시글의 좋아요 개수({@code likeCount})를 증감
     *
     * @param member  좋아요를 토글할 회원 {@link Member} 객체
     * @param postId    좋아요를 토글할 게시글의 고유 ID
     * @param hasLike   현재 좋아요 상태. {@code true}이면 좋아요를 취소하고, {@code false}이면 좋아요를 추가
     */
    @Transactional
    public void toggle(Member member, Long postId, Boolean hasLike) {
        Post post = entityFinder.getPost(postId);

        if (hasLike) {
            delete(member, post);
            post.decrementLikeCount();
        } else {
            save(member, post);
            post.incrementLikeCount();
        }
    }
}
