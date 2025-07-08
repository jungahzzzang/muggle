package com.curty.muggle.post.repository;

import com.curty.muggle.post.dto.response.PostResponse;
import com.curty.muggle.post.entity.PostCategory;
import com.curty.muggle.post.entity.PostSortCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCustomRepository {
    Page<PostResponse> getAll(
            Pageable pageable,
            PostSortCondition sort,
            PostCategory category,
            String keyword
    );
}
