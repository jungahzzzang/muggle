package com.curty.muggle.post.dto.request;

import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.post.entity.PostCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostAddRequest {
    private String title;

    private String content;

    private String category;

    private List<MultipartFile> images;

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .category(PostCategory.valueOf(this.category))
                .title(this.title)
                .content(this.content)
                .build();
    }
}
