package com.curty.muggle.post.dto.response;

import com.curty.muggle.post.entity.PostCategory;
import com.curty.muggle.post.entity.PostState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class PostResponse {
    private Long postId;

    private Long memberId;

    private PostState state;

    private PostCategory category;

    private String title;

    private String content;

    private String memberNickname;

    private int likeCount;

    private int viewCount;

    private LocalDateTime createdAt;

    private Long commentCount;
}
