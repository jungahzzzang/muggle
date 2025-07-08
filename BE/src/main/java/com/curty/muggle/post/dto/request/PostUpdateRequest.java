package com.curty.muggle.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequest {
    private String title;

    private String content;

    private String category;

    private List<String> imagesToDelete;

    private List<MultipartFile> newImages;
}
