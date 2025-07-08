package com.curty.muggle.post.service;

import com.curty.muggle.common.exception.ErrorCode;
import com.curty.muggle.common.exception.custom.PostAccessException;
import com.curty.muggle.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostAuthorCheckService {
    private final PostRepository postRepository;

    /**
     * 게시글 수정 및 삭제 권한을 검증하는 메서드
     * 현재 로그인한 사용자(userId)가 게시글의 작성자(authorId)인지 확인

     * @param userId 업데이트를 요청한 사용자의 ID
     * @param postId 업데이트하려는 게시글의 ID
     * @throws PostAccessException 사용자가 게시글의 작성자가 아닐 경우
     */
    public void checkAuthor(Long userId, Long postId) {
        Long authorId = postRepository.getAuthorId(postId);

        if (!Objects.equals(authorId, userId)) {
            throw new PostAccessException(ErrorCode.CANNOT_UPDATE_OTHERS_POST);
        }
    }
}
