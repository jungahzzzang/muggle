package com.curty.muggle.common.security;

import com.curty.muggle.common.jwt.JwtProvider;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 토큰을 검증하고 인증 정보를 SecurityContext에 설정하는 필터
 * 모든 요청에 대해 JWT 유효성을 체크하여 인증 처리 수행
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    /**
     * 요청에서 JWT 토큰을 추출하고, 토큰이 유효하면 인증 정보를 SecurityContext에 설정
     * 이후 필터 체인을 계속 진행시켜서 다음 필터나 컨트롤러 실행으로 연결
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException, IOException 필터 처리 중 발생할 수 있는 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = resolveToken(request);

            if (jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmailFromToken(token);
                Member member = memberService.findByEmail(email);
                UserDetails userDetails = new KakaoMemberDetails(member);
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 "Authorization" 헤더를 찾아 "Bearer " 접두사를 제거한 JWT 토큰 문자열을 반환
     *
     * @param request HTTP 요청 객체
     * @return 추출한 JWT 토큰 문자열 또는 토큰이 없거나 형식이 올바르지 않으면 null 반환
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
