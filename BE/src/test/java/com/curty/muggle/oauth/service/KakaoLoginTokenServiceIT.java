package com.curty.muggle.oauth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.curty.muggle.common.config.properties.KakaoOAuthRegistrationProperties;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class KakaoLoginTokenServiceIT {
    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Autowired
    private KakaoOAuthRegistrationProperties kakaoOAuthRegistrationProperties;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Test
    void testGetKakaoInfo() {
        Map<String, String> response = kakaoLoginService.getKakaoInfo();
        ResponseEntity<?> result = ResponseEntity.ok(response);
        
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        Map<String, String> body = (Map<String, String>) result.getBody();

        assertNotNull(result.getBody());
        assertEquals(kakaoOAuthRegistrationProperties.getClientId(), body.get("client_id"));
        assertEquals(kakaoOAuthRegistrationProperties.getRedirectUri(), body.get("redirect_uri"));
    }
}
