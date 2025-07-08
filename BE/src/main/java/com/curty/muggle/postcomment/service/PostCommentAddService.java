package com.curty.muggle.postcomment.service;

import com.curty.muggle.postcomment.dto.request.PostCommentAddRequest;
import com.curty.muggle.postcomment.entity.PostComment;
import com.curty.muggle.postcomment.repository.PostCommentRepository;
import com.curty.muggle.common.component.EntityFinder;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostCommentAddService {
    private final PostCommentRepository postCommentRepository;
    private final EntityFinder entityFinder;

    /**
     * 새로운 댓글을 등록하는 메서드
     *
     * @param member  댓글을 작성하는 멤버 {@link Member} 객체
     * @param postId  댓글이 추가될 게시글의 ID
     * @param request 댓글 추가에 필요한 데이터를 담고 있는 {@link PostCommentAddRequest} DTO 객체.
     */
    @Transactional
    public void add(Member member, Long postId, PostCommentAddRequest request) {
        Post post = entityFinder.getPost(postId);

        PostComment postComment = PostComment.builder()
                .member(member)
                .post(post)
                .parentId(request.getParentId())
                .content(request.getContent())
                .build();

        postCommentRepository.save(postComment);
    }
}
