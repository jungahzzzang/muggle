package com.curty.muggle.oauth.controller;

import com.curty.muggle.common.security.KakaoMemberDetails;
import com.curty.muggle.member.dto.response.MemberResponse;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.service.MemberService;
import com.curty.muggle.oauth.dto.KakaoLoginToken;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse;
import com.curty.muggle.common.jwt.JwtProvider;
import com.curty.muggle.oauth.service.KakaoLoginService;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 카카오 로그인 API 요청 처리 컨트롤러
 */
@Tag(name = "Kakao Login Controller", description = "Kakao Login Controller desc")
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;
    private final Logger logger = LoggerFactory.getLogger(KakaoLoginController.class);
    private final JwtProvider jwtProvider;

    /**
     * 카카오 로그인 동의 화면으로 리다이렉트하기 위한 필수 파라미터를 반환
     *
     * @return 카카오 로그인 요청에 필요한 파라미터들(client_id, redirect_uri 등)을 담은 Map 객체
     */
    @Operation(summary = "카카오 로그인 동의 화면으로의 이동을 위한 info", description = "카카오 로그인 버튼 클릭시 작동")
    @GetMapping("/kakao-info")
    public ResponseEntity<Map<String,String>> getKakaoInfo(){
        Map<String,String> params =  kakaoLoginService.getKakaoInfo();
        return ResponseEntity.ok(params);
    }

    /**
     * 카카오 로그인 처리 메서드
     *
     * @param code 카카오 로그인 인가 코드
     * @return JWT 액세스 토큰과 회원 정보 응답 dto{@link MemberResponse}를 담은 응답 객체
     */
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 진행")
    @GetMapping("/callback/kakao")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> kakaoLogin (@RequestParam String code) {
        KakaoLoginToken tokenResponse = kakaoLoginService.handleKakaoLogin(code);

        if (tokenResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 인증 실패: access_token이 없습니다.");
        }

        String accessToken = tokenResponse.getAccessToken();
        KakaoUserInfoResponse user = kakaoLoginService.handleKakaoUserInfo(accessToken); // 유저 정보 가져오기
        Member member = memberService.findOrCreate(user); // 기존 회원 로그인, 신규 회원 가입

        UserDetails userDetails = new KakaoMemberDetails(member);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtProvider.generateToken(member.getEmail());

        Map<String, Object> result = new HashMap<>();
        result.put("access_token", jwtToken);
        result.put("member", new MemberResponse(member));
        return ResponseEntity.ok(result);
    }
}