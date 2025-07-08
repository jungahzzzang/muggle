package com.curty.muggle.postcomment.service;

import com.curty.muggle.postcomment.dto.response.PostCommentResponse;
import com.curty.muggle.postcomment.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostCommentListService {
    private final PostCommentRepository postCommentRepository;
    private static final Integer COMMENT_PAGE_SIZE = 5;

    /**
     * 특정 게시글의 댓글 목록을 커서 기반 페이지네이션으로 조회하는 메서드
     *
     * @param postId 댓글을 조회할 게시글의 고유 ID
     * @param cursorId 페이지네이션 커서 역할을 하는 댓글 ID. 이 ID보다 작은 ID를 가진 댓글들을 조회
     * 첫 페이지 조회 시에는 {@code null}을 전달하여 가장 최신 댓글부터 조회
     * @return 조회된 {@link PostCommentResponse} 객체들의 리스트
     */
    @Transactional(readOnly = true)
    public List<PostCommentResponse> getAll(Long postId, Long cursorId) {
        return postCommentRepository.getAll(
                COMMENT_PAGE_SIZE,
                postId,
                (cursorId != null) ? cursorId : Long.MAX_VALUE
        );
    }
}
