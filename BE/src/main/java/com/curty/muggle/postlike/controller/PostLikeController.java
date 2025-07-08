package com.curty.muggle.postlike.controller;

import com.curty.muggle.common.security.UserPrincipal;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.postlike.service.PostLikeCheckService;
import com.curty.muggle.postlike.service.PostLikeToggleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post-Like Controller", description = "게시글 좋아요 관련 API를 담당하는 컨트롤러")
public class PostLikeController {
    private final PostLikeCheckService postLikeCheckService;
    private final PostLikeToggleService postLikeToggleService;

    /**
     * 특정 게시글에 대한 현재 사용자의 좋아요 여부를 확인하는 메서드
     *
     * @param postId 좋아요 여부를 확인할 게시글의 고유 ID
     * @return HTTP 200 OK 상태 코드와, 해당 게시글에 좋아요를 눌렀으면 {@code true}, 그렇지 않으면 {@code false}
     */
    @GetMapping("/api/posts/{postId}/like/check")
    @Operation(summary = "좋아요 여부 확인", description = "특정 게시글에 대한 해당 유저의 좋아요 여부를 확인")
    ResponseEntity<Boolean> check(
            @UserPrincipal Member member,
            @PathVariable Long postId
    ) {
        boolean hasLike = postLikeCheckService.check(member.getMemberId(), postId);

        return ResponseEntity.ok(hasLike);
    }

    /**
     * 특정 게시글에 대한 좋아요를 추가하거나 취소하는 메서드
     * {@code hasLike} 파라미터 값에 따라 좋아요를 추가하거나 취소하는 로직을 서비스 계층에서 처리
     *
     * @param postId  좋아요를 토글할 게시글의 고유 ID
     * @param hasLike 좋아요를 추가할 경우 {@code true}, 좋아요를 취소할 경우 {@code false}
     * @return 로직 성공 시 HTTP 200 OK 상태 코드와 빈 응답 본문 반환
     */
    @PostMapping("/api/posts/{postId}/like")
    @Operation(summary = "좋아요/좋아요 취소", description = "특정 게시글에 대한 좋아요 및 좋아요 취소")
    ResponseEntity<Object> toggle(
            @UserPrincipal Member member,
            @PathVariable Long postId,
            @RequestParam Boolean hasLike
    ) {
        postLikeToggleService.toggle(member, postId, hasLike);

        return ResponseEntity.ok(Collections.EMPTY_MAP);
    }
}
