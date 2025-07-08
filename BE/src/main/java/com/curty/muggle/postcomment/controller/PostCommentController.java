package com.curty.muggle.postcomment.controller;

import com.curty.muggle.common.security.UserPrincipal;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.postcomment.dto.request.PostCommentAddRequest;
import com.curty.muggle.postcomment.dto.response.PostCommentResponse;
import com.curty.muggle.postcomment.service.PostCommentAddService;
import com.curty.muggle.postcomment.service.PostCommentListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post-Comment Controller", description = "게시글 댓글 관련 API를 담당하는 컨트롤러")
public class PostCommentController {
    private final PostCommentAddService postCommentAddService;
    private final PostCommentListService postCommentListService;

    @PostMapping("/api/posts/{postId}/comments")
    @Operation(summary = "댓글 등록", description = "댓글 등록")
    ResponseEntity<Object> add(
            @UserPrincipal Member member,
            @PathVariable Long postId,
            @RequestBody PostCommentAddRequest request
    ) {
        postCommentAddService.add(member, postId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.EMPTY_MAP);
    }

    @GetMapping("/api/posts/{postId}/comments")
    @Operation(summary = "댓글 조회", description = "댓글 전체 목록 조회 (커서 기반 페이징")
    ResponseEntity<List<PostCommentResponse>> getAll(
            @PathVariable Long postId,
            @RequestParam(required = false) Long cursorId
    ) {
        List<PostCommentResponse> contents = postCommentListService.getAll(postId, cursorId);

        return ResponseEntity.ok(contents);
    }
}
