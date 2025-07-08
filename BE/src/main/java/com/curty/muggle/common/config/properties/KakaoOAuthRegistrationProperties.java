package com.curty.muggle.common.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
public class KakaoOAuthRegistrationProperties {
    
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationGrantType;
    private List<String> scope;
    private String clientName;
}
