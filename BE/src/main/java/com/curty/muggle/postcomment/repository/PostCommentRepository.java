package com.curty.muggle.postcomment.repository;

import com.curty.muggle.postcomment.dto.response.PostCommentResponse;
import com.curty.muggle.postcomment.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query("SELECT new com.curty.muggle.postcomment.dto.response.PostCommentResponse(" +
            "pc.commentId," +
            "pc.parentId," +
            "pc.member.memberId," +
            "pc.member.nickname," +
            "pc.content," +
            "pc.createdAt," +
            "pc.updatedAt" +
            ") " +
            "FROM PostComment pc " +
            "WHERE pc.post.postId = :postId AND pc.commentId < :cursorId " +
            "ORDER BY pc.commentId DESC " +
            "LIMIT :pageSize")
    List<PostCommentResponse> getAll(int pageSize, Long postId, Long cursorId);
}
