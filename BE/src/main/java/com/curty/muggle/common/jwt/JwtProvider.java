package com.curty.muggle.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰 생성, 검증, 정보 추출을 담당하는 유틸리티 클래스
 */
@Component
@Slf4j
public class JwtProvider {
    private SecretKey key = Jwts.SIG.HS256.key().build();
    private final long expiration = 1000 * 60 * 60; // 1시간
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 이메일을 기반으로 JWT 액세스 토큰을 생성
     * @param email 사용자 이메일
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(String email) {
        logger.info("[jwt 엑세스 토큰 생성]");
        return Jwts.builder()
                .header()                                   // (2) optional
                    .keyId("aKeyId")
                    .and()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * JWT 토큰의 유효성을 검사
     * 만료됐거나 위조된 토큰일 경우 예외 발생
     * @param token 검사할 JWT 토큰 문자열
     * @return 유효하면 true, 아니면 예외 처리
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> jwt = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            Object payload = jwt.getPayload();
            log.info("[jwt 페이로드] {}", payload);
            return true;
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException("[에러] 만료된 토큰" + e.getMessage());
        } catch (JwtException e) {
            throw new IllegalStateException("[에러] 토큰 읽기 실패" + e.getMessage());
        }
    }

    /**
     * JWT 토큰에서 사용자 이메일을 추출
     * @param token JWT 토큰 문자열
     * @return 토큰에 포함된 이메일
     */
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (JwtException e) {
            throw new IllegalStateException("[에러] 이메일 추출 실패" + e.getMessage());
        }
    }
}
