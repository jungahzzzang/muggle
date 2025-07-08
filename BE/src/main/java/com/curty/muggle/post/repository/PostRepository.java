package com.curty.muggle.post.repository;

import com.curty.muggle.post.dto.response.PostDetailResponse;
import com.curty.muggle.post.dto.response.PostFormResponse;
import com.curty.muggle.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    @Query("SELECT new com.curty.muggle.post.dto.response.PostDetailResponse(" +
            "p.postId," +
            "p.member.memberId," +
            "p.state," +
            "p.category," +
            "p.title," +
            "p.content," +
            "p.member.nickname," +
            "p.viewCount," +
            "p.likeCount," +
            "p.createdAt," +
            "p.updatedAt" +
            ") " +
            "FROM Post p " +
            "WHERE p.postId = :postId")
    PostDetailResponse get(Long postId);

    @Query("SELECT new com.curty.muggle.post.dto.response.PostFormResponse(" +
            "p.postId," +
            "p.title," +
            "p.content," +
            "p.category" +
            ") " +
            "FROM Post p " +
            "WHERE p.postId = :postId")
    PostFormResponse getFormInfo(Long postId);

    @Query("SELECT p.member.memberId FROM Post p WHERE p.postId = :postId")
    Long getAuthorId(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
    void incrementViewCount(Long postId);
}
