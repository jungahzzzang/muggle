package com.curty.muggle.post.controller;

import com.curty.muggle.common.security.UserPrincipal;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.dto.request.PostAddRequest;
import com.curty.muggle.post.dto.request.PostUpdateRequest;
import com.curty.muggle.post.dto.response.PostDetailResponse;
import com.curty.muggle.post.dto.response.PostFormResponse;
import com.curty.muggle.post.dto.response.PostResponse;
import com.curty.muggle.post.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "게시글 관련 API를 담당하는 컨트롤러")
public class PostController {
    private final PostAddService postAddService;
    private final PostAuthorCheckService postAuthorCheckService;
    private final PostDeleteService postDeleteService;
    private final PostGetService postGetService;
    private final PostGetUpdateFormService postGetUpdateFormService;
    private final PostListService postListService;
    private final PostUpdateService postUpdateService;

    private final String POST_VIEW_COOKIE_NAME_PATTERN = "post-%s-view";

    /**
     * 특정 게시글의 조회수 증가를 위한 쿠키가 요청에 존재하는지 확인하는 내부 메서드
     *
     * @param request 현재 HTTP 요청
     * @param postId  조회 대상 게시글의 고유 ID
     * @return 해당 게시글의 조회수 쿠키가 존재하면 {@code true}, 그렇지 않으면 {@code false}
     */
    private boolean hasCookieOfPostView(HttpServletRequest request, Long postId) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(POST_VIEW_COOKIE_NAME_PATTERN.formatted(postId))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 특정 게시글의 조회수 증가를 위한 쿠키를 응답에 추가하는 내부 메서드
     * 이 쿠키는 24시간 동안 유효
     *
     * @param response 현재 HTTP 응답
     * @param postId   조회 대상 게시글의 고유 ID
     */
    private void addCookieOfPostView(HttpServletResponse response, Long postId) {
        Cookie cookie = new Cookie(POST_VIEW_COOKIE_NAME_PATTERN.formatted(postId), "-");

        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24시간

        response.addCookie(cookie);
    }

    /**
     * 새로운 게시글을 생성하는 POST 요청을 처리하는 메서드
     *
     * @param request 생성할 게시글의 데이터를 담은 {@link PostAddRequest} 객체
     * @return HTTP 201 Created 상태 코드와 빈 응답 본문
     */
    @PostMapping("/api/posts")
    @Operation(summary = "게시글 등록", description = "제목, 내용, 카테고리 값을 받아 게시글 등록")
    ResponseEntity<Object> add(
            @UserPrincipal Member member,
            @ModelAttribute PostAddRequest request
    ) {
        postAddService.add(request, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.EMPTY_MAP);
    }

    /**
     * 특정 ID를 가진 게시글의 상세 정보를 조회하는 GET 요청을 처리하는 메서드
     * 클라이언트 요청에 조회수 쿠키가 없는 경우, 쿠키를 추가하고,
     * {@link PostGetService}를 통해 게시글 조회수를 증가시킨 후 상세 정보를 반환합니다.
     *
     * @param postId   조회할 게시글의 고유 ID
     * @param request  현재 HTTP 요청 객체 (조회수 쿠키 확인용)
     * @param response 현재 HTTP 응답 객체 (조회수 쿠키 추가용)
     * @return {@link PostDetailResponse} 객체를 포함하는 {@link ResponseEntity}. 조회 성공 시 HTTP 200 OK 상태 코드 반환.
     */
    @GetMapping("/api/posts/{postId}")
    @Operation(summary = "게시글 단건 조회", description = "특정 ID에 해당 하는 게시글 상세 정보를 조회하고, 최초 조회 시 조회수를 증가")
    ResponseEntity<PostDetailResponse> get(
            @PathVariable Long postId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 조회수 쿠키 존재 여부 확인
        boolean hasCookieOfPostView = hasCookieOfPostView(request, postId);

        // 쿠키가 없다면 추가
        if (!hasCookieOfPostView) {
            addCookieOfPostView(response, postId);
        }

        PostDetailResponse post = postGetService.get(postId, !hasCookieOfPostView);

        return ResponseEntity.ok(post);
    }

    /**
     * 다양한 조건(페이징, 정렬, 카테고리, 키워드)에 따라 게시글 목록을 조회하는 메서드
     *
     * @param page     현재 페이지 번호 (기본값: 1)
     * @param sort     게시글 정렬 조건 (기본값: LATEST)
     * @param category 게시글 카테고리 (선택 사항)
     * @param keyword  게시글 제목 또는 내용에 포함될 검색 키워드 (선택 사항)
     * @return {@link PostResponse} 객체를 포함하는 {@link Page} 객체와 HTTP 200 OK 상태 코드
     */
    @GetMapping("/api/posts")
    @Operation(summary = "게시글 목록 가져오기", description = "게시글 목록 가져오기 (페이징, 정렬, 카테고리, 키워드를 활용)")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호"),
            @Parameter(name = "sort", description = "정렬 조건 (LATEST, OLDEST, MOST_VIEWED, MOST_LIKED"),
            @Parameter(name = "category", description = "게시글 카테고리 (선택 사항) (ETC, INFORMATION, QUESTION, REVIEW"),
            @Parameter(name = "keyword", description = "검색 키워드 (선택 사항)")
    })
    ResponseEntity<Page<PostResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "LATEST") String sort,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword
    ) {
        Page<PostResponse> posts = postListService.getAll(page, sort, category, keyword);

        return ResponseEntity.ok(posts);
    }

    /**
     * 특정 게시글의 정보를 업데이트하는 메서드
     *
     * @param postId  업데이트할 게시글의 ID
     * @param request 업데이트할 내용을 담은 요청 본문 DTO ({@link PostUpdateRequest})
     * @return HTTP 200 OK 상태 코드와 빈 응답 본문
     */
    @PutMapping("/api/posts/{postId}")
    @Operation(summary = "게시글 수정", description = "특정 ID에 해당하는 게시글을 수정")
    ResponseEntity<Object> update(
            @UserPrincipal Member member,
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequest request
    ) {
        postUpdateService.update(postId, request, member.getMemberId());

        return ResponseEntity.ok(Collections.EMPTY_MAP);
    }

    /**
     * 게시글 수정 시 필요한 폼 정보를 조회하는 메서드
     *
     * @param postId 조회할 게시글의 ID
     * @return 업데이트할 게시글의 기본 정보를 담은 요청 본문 DTO ({@link PostFormResponse})
     * HTTP 상태 코드 200 (OK)와 함께 게시글 폼 정보가 성공적으로 반환됩니다.
     **/
    @GetMapping("/api/posts/{postId}/form")
    @Operation(summary = "게시글 수정 시 폼 정보 조회", description = "게시글 수정 시 필요한 폼 정보를 조회")
    ResponseEntity<PostFormResponse> getFormInfo(
            @PathVariable Long postId
    ) {
        PostFormResponse postFormResponse = postGetUpdateFormService.getFormInfo(postId);

        return ResponseEntity.ok(postFormResponse);
    }

    /**
     * 특정 게시글을 삭제하는 메서드
     *
     * @param postId 삭제할 게시글의 ID
     * @return 삭제 성공 시 HTTP 200 OK 상태 코드와 빈 응답 본문 반환
     */
    @DeleteMapping("/api/posts/{postId}")
    @Operation(summary = "게시글 삭제", description = "특정 ID에 해당하는 게시글을 삭제")
    ResponseEntity<Object> delete(
            @UserPrincipal Member member,
            @PathVariable Long postId
    ) {
        postDeleteService.delete(postId, member.getMemberId());

        return ResponseEntity.ok(Collections.EMPTY_MAP);
    }

    @GetMapping("/api/posts/{postId}/author/check")
    @Operation(summary = "게시글 작성자 확인", description = "접근 유저가 해당 게시글의 작성자인지 확인")
    ResponseEntity<Object> checkAuthor(
            @UserPrincipal Member member,
            @PathVariable Long postId
    ) {
        postAuthorCheckService.checkAuthor(member.getMemberId(), postId);

        return ResponseEntity.ok(Collections.EMPTY_MAP);
    }
}
