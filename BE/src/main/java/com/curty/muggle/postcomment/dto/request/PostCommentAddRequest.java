package com.curty.muggle.postcomment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentAddRequest {
    private Long parentId;

    private String content;
}
