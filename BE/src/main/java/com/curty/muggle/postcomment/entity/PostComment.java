package com.curty.muggle.postcomment.entity;

import com.curty.muggle.common.entity.BaseTime;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(name = "POST_COMMENT")
public class PostComment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_COMMENT_ID", nullable = false, unique = true)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "PARENT_ID", nullable = true)
    private Long parentId;

    @Column(name = "CONTENT", nullable = false)
    private String content;
}
