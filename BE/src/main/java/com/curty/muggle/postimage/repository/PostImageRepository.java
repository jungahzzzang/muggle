package com.curty.muggle.postimage.repository;

import com.curty.muggle.postimage.dto.response.PostImageResponse;
import com.curty.muggle.postimage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, UUID> {
    @Query("SELECT new com.curty.muggle.postimage.dto.response.PostImageResponse(" +
            "   pi.postImageId, " +
            "   pi.originalImageName, " +
            "   pi.imageUrl" +
            ") " +
            "FROM PostImage pi " +
            "WHERE pi.post.postId = :postId")
    List<PostImageResponse> getAll(Long postId);
}
