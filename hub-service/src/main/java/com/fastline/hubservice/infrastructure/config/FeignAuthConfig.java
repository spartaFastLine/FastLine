package com.fastline.hubservice.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignAuthConfig {

	@Bean
	public RequestInterceptor jwtInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate template) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null && auth.getCredentials() != null) {
					String token = auth.getCredentials().toString();
					template.header("Authorization", "Bearer " + token);
				}
			}
		};
	}
}
