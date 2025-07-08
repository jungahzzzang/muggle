package com.curty.muggle.postlike.repository;

import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.postlike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query("SELECT count(pl.postLikeId) " +
            "FROM PostLike pl " +
            "WHERE pl.member.memberId = :memberId AND pl.post.postId = :postId")
    Long hasLike(Long memberId, Long postId);

    void deleteByMemberAndPost(Member member, Post post);
}
