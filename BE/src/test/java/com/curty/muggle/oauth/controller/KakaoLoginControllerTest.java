package com.curty.muggle.oauth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.curty.muggle.common.jwt.JwtProvider;
import com.curty.muggle.member.dto.response.MemberResponse;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.entity.MemberRole;
import com.curty.muggle.member.entity.MemberState;
import com.curty.muggle.member.service.MemberService;
import com.curty.muggle.oauth.dto.KakaoLoginToken;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse.KakaoAccount;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse.KakaoAccount.Profile;
import com.curty.muggle.oauth.service.KakaoLoginService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
public class KakaoLoginControllerTest {
    @InjectMocks
    private KakaoLoginController kakaoLoginController;

    @Mock
    KakaoLoginService kakaoLoginService;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtProvider jwtProvider;

    @Test
    void getKakaoInfo_파라미터반환() {
        //given
        String clientId = "test-client-id";
        String redirectUri = "test-redirect-uri";
        Map<String,String> mockResponse = new HashMap<>();
        mockResponse.put("client_id", clientId);
        mockResponse.put("redirect_uri", redirectUri);
        when(kakaoLoginService.getKakaoInfo()).thenReturn(mockResponse);

        //when
        ResponseEntity<?> response = kakaoLoginController.getKakaoInfo();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testLogin_카카오로그인전체() {
        //given
        String code = "code";
        String accessToken = "accessToken";
        String email = "email";
        String nickname = "nickname";
        String jwt = "jwt";
        Member mockMember = new Member(null, MemberRole.USER, MemberState.ACTIVE, email, "nickname");
        KakaoLoginToken mockToken = new KakaoLoginToken("bearer", accessToken, null, 3600, "refresh", 7200, null);
        KakaoUserInfoResponse mockUserInfo = new KakaoUserInfoResponse(null, new KakaoAccount(email, new Profile(nickname)));

        when(kakaoLoginService.handleKakaoLogin(code)).thenReturn(mockToken);
        when(kakaoLoginService.handleKakaoUserInfo(mockToken.getAccessToken())).thenReturn(mockUserInfo);
        when(memberService.findOrCreate(mockUserInfo)).thenReturn(mockMember);
        when(jwtProvider.generateToken(email)).thenReturn(jwt);

        //when
        ResponseEntity<?> response = kakaoLoginController.kakaoLogin(code);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assert body != null;
        assertEquals(jwt, body.get("access_token"));
        assertInstanceOf(MemberResponse.class, body.get("member"));
    }
}
