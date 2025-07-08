package com.curty.muggle.oauth.service;

import com.curty.muggle.oauth.dto.KakaoLoginToken;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.curty.muggle.common.config.properties.KakaoOAuthProviderProperties;
import com.curty.muggle.common.config.properties.KakaoOAuthRegistrationProperties;

/**
 * 카카오 로그인 관련 비즈니스 로직 처리 서비스
 */
@Service
@Slf4j
public class KakaoLoginService {
    private final RestClient restClient;
    private final KakaoOAuthRegistrationProperties kakaoOAuthRegistrationProperties;
    private final KakaoOAuthProviderProperties kakaoOAuthProviderProperties;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public KakaoLoginService(RestClient restClient,
                             KakaoOAuthRegistrationProperties kakaoOAuthRegistrationProperties,
                             KakaoOAuthProviderProperties kakaoOAuthProviderProperties) {
        this.restClient = restClient;
        this.kakaoOAuthRegistrationProperties = kakaoOAuthRegistrationProperties;
        this.kakaoOAuthProviderProperties = kakaoOAuthProviderProperties;
    }

    /**
     * 카카오 로그인 동의 화면으로 리다이렉트하기 위한 필수 파라미터 세팅
     *
     * @return 카카오 OAuth 요청에 필요한 파라미터들(client_id, redirect_uri 등)을 담은 Map 객체
     */
    public Map<String, String> getKakaoInfo(){
        Map<String,String> response = new HashMap<>();
        log.info("[카카오 로그인 info 요청]");
        response.put("client_id", kakaoOAuthRegistrationProperties.getClientId());
        response.put("redirect_uri", kakaoOAuthRegistrationProperties.getRedirectUri());

        return response;
    }

    /**
     * 카카오 로그인 엑세스 토큰 요청
     *
     * @param code 카카오 로그인 인가 코드
     * @return 카카오 로그인 엑세스 토큰 정보를 담은 dto {@link KakaoLoginToken}
     */
    public KakaoLoginToken handleKakaoLogin(String code) {
        MultiValueMap<String, String> params = setTokenParams(code);
        log.info("[카카오 token 발급 요청] {}", params);

        return postKakaoRequest(params);
    }

    /**
     * 카카오 엑세스 토큰 값을 기반으로 회원 정보를 요청
     *
     * @param accessToken 카카오 엑세스 토큰
     * @return 회원 정보를 담은 응답 객체 {@link KakaoUserInfoResponse}
     */
    public KakaoUserInfoResponse handleKakaoUserInfo(String accessToken) {
        log.info("[카카오 회원 정보 요청] 엑세스 토큰 : {}", accessToken);
        return restClient.post()
                .uri(kakaoOAuthProviderProperties.getUserInfoUri())
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    httpHeaders.setBearerAuth(accessToken);
                })
                .retrieve()
                .body(KakaoUserInfoResponse.class);
    }

    /**
     * 카카오 엑세스 토큰 발급 요청
     *
     * @param tokenRequest 엑세스 토큰 요청을 위한 파라미터(client_id, redirect_uri, code, grant_type)를 담은 Map 객체
     * @return 카카오 엑세스 토큰 발급 응답을 담은 dto 객체 {@link KakaoLoginToken}
     */
    private KakaoLoginToken postKakaoRequest(MultiValueMap<String, String> tokenRequest) {
        return restClient.post()
                .uri(kakaoOAuthProviderProperties.getTokenUri())
                .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .body(tokenRequest)
                .retrieve()
                .body(KakaoLoginToken.class);
    }

    /**
     * 카카오 엑세스 토큰 발급을 위한 파라미터 세팅
     *
     * @param code 카카오 로그인 인가 코드
     * @return 엑세스 토큰 요청을 위한 파라미터(client_id, redirect_uri, code, grant_type)를 담은 Map 객체
     */
    private MultiValueMap<String, String> setTokenParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", kakaoOAuthRegistrationProperties.getClientId());
        params.add("redirect_uri", kakaoOAuthRegistrationProperties.getRedirectUri());
        params.add("code", code);
        params.add("grant_type", "authorization_code");

        return params;
    }
}