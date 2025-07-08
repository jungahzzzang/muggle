package com.curty.muggle.postcomment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class PostCommentResponse {
    private Long commentId;

    private Long parentId;

    private Long memberId;

    private String memberNickname;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
