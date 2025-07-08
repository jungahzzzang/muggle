package com.curty.muggle.post.dto.response;

import com.curty.muggle.post.entity.PostCategory;
import com.curty.muggle.postimage.dto.response.PostImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostFormResponse {
    private Long postId;

    private String title;

    private String content;

    private PostCategory category;

    private List<PostImageResponse> images;

    public PostFormResponse(Long postId, String title, String content, PostCategory category) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
