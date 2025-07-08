package com.curty.muggle.common.component;

import com.curty.muggle.common.exception.ErrorCode;
import com.curty.muggle.common.exception.custom.EntityNotFoundException;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.repository.MemberRepository;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /**
     * 주어진 회원 ID로 회원을 조회하는 내부 메서드
     * 회원이 존재하지 않을 경우 {@link EntityNotFoundException}을 발생
     *
     * @param memberId 조회할 회원의 고유 ID
     * @return 조회된 {@link Member} 객체
     * @throws EntityNotFoundException 회원이 존재하지 않을 경우
     */
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 주어진 게시글 ID로 게시글을 조회하는 내부 메서드
     * 게시글이 존재하지 않을 경우 {@link EntityNotFoundException}을 발생
     *
     * @param postId 조회할 게시글의 고유 ID
     * @return 조회된 {@link Post} 객체
     * @throws EntityNotFoundException 게시글이 존재하지 않을 경우
     */
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
    }
}
