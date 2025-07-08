package com.curty.muggle.common.config;

import com.curty.muggle.common.security.UserPrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{
	private final UserPrincipalArgumentResolver userPrincipalArgumentResolver;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry
						.addMapping("/**")
						//.allowedOrigins("/*")	//외부에서 들어오는 모든 url을 허용
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")	//허용되는 Method
						.allowedHeaders("*")	//허용되는 Header
						.exposedHeaders("Authorization","Refresh-Token")
						.allowCredentials(true)	//자격증명 허용
						.maxAge(3600);			//허용 시간
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userPrincipalArgumentResolver);
	}
}
