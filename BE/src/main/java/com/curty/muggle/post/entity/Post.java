package com.curty.muggle.post.entity;

import com.curty.muggle.common.entity.BaseTime;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.dto.request.PostUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@Builder
@DynamicUpdate
@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "POST",
        indexes = @Index(name = "idx_post_member_id", columnList = "member_id")
)
public class Post extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID", nullable = false, unique = true)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    private PostState state = PostState.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY", nullable = false)
    private PostCategory category;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Builder.Default
    @Column(name = "LIKE_COUNT", nullable = false)
    private int likeCount = 0;

    @Builder.Default
    @Column(name = "VIEW_COUNT", nullable = false)
    private int viewCount = 0;

    public void update(PostUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.category = PostCategory.valueOf(request.getCategory());
    }

    public void delete() {
        this.state = PostState.DELETED;
        this.title = "삭제된 게시글입니다.";
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }
}