package com.curty.muggle.post.dto.response;

import com.curty.muggle.post.entity.PostCategory;
import com.curty.muggle.post.entity.PostState;
import com.curty.muggle.postimage.dto.response.PostImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDetailResponse {
    private Long postId;

    private Long memberId;

    private PostState state;

    private PostCategory category;

    private String title;

    private String content;

    private String memberName;

    private int viewCount;

    private int likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<PostImageResponse> images;

    public PostDetailResponse(
            Long postId,
            Long memberId,
            PostState state,
            PostCategory category,
            String title,
            String content,
            String memberName,
            int viewCount,
            int likeCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.postId = postId;
        this.memberId = memberId;
        this.state = state;
        this.category = category;
        this.title = title;
        this.content = content;
        this.memberName = memberName;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
