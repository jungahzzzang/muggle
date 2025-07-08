package com.curty.muggle.postimage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class PostImageResponse {
    private UUID postImageId;

    private String originalImageName;

    private String imageUrl;
}
