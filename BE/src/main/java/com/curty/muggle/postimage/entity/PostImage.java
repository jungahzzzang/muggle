package com.curty.muggle.postimage.entity;

import com.curty.muggle.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(name = "POST_IMAGE")
public class PostImage {
    @Id
    @Column(name = "POST_IMAGE_ID", nullable = false, unique = true)
    private UUID postImageId;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @Column(name = "ORIGINAL_IMAGE_NAME", nullable = false)
    private String originalImageName;

    @Column(name = "IMAGE_URL", nullable = false)
    private String imageUrl;

    @Column(name = "IMAGE_SIZE", nullable = false)
    private Long imageSize;

    @Column(name = "IMAGE_TYPE", nullable = false)
    private String imageType;
}
