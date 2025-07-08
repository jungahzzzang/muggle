package com.curty.muggle.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 로그인 API 응답에서 받은 OAuth 토큰 정보를 담는 DTO 클래스
 *
 * 주요 필드:
 * - tokenType: 토큰 타입 (예: "Bearer")
 * - accessToken: 액세스 토큰 (API 접근 권한용)
 * - idToken: ID 토큰 (nullable, 인증 정보 포함 가능)
 * - expiresIn: 액세스 토큰 만료 시간 (초 단위)
 * - refreshToken: 리프레시 토큰 (액세스 토큰 갱신용)
 * - refreshTokenExpiresIn: 리프레시 토큰 만료 시간 (초 단위)
 * - scope: 권한 범위 (nullable)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginToken {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("id_token")
    private String idToken; // Nullable

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope; // Nullable
}