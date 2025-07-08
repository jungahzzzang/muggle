package com.curty.muggle.common.config;

import com.curty.muggle.common.config.properties.ClientProperties;
import com.curty.muggle.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security 전반 설정 클래스
 * - JWT 인증 필터 등록
 * - CORS 정책 설정
 * - CSRF 비활성화
 * - 엔드포인트 접근 권한 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private final ClientProperties clientProperties;

    /**
     * Spring Security 보안 필터 체인 설정
     * - JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 실행되도록 추가
     * - 특정 경로에 대한 접근 허용 설정
     * - CSRF 비활성화 및 CORS 설정
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 예외 발생 시 처리
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/",
                                        "/index.html",
                                        // Swagger
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        // 소셜 로그인
                                        "/oauth/callback/kakao",
                                        "/oauth/kakao-info",
                                        // API
                                        "/api/musical/**",
                                        "/api/member/**",
                                        "/api/theater/**",
                                        "/api/review/**",
                                        "/api/scheduler/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/posts/{postId}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/posts/{postId}/form").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/posts/{postId}/comments").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정을 위한 Bean 등록
     * - 등록만 하면 필터체인에서 자동으로 사용
     * - 허용 Origin, 메서드, 헤더, Credential 설정
     *
     * @return CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> allowedOrigins = clientProperties.getReactUrls();

        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            throw new IllegalStateException("CORS 허용 origin이 비어 있습니다.");
        }
        configuration.setAllowedOrigins(allowedOrigins); //프론트 주소
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정 적용
        return source;
    }
}