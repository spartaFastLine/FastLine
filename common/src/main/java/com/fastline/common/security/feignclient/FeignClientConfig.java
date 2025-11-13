package com.fastline.common.security.feignclient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		// Gemini API 호출 제외
		if (requestTemplate.feignTarget().url().contains("generativelanguage.googleapis.com")) {
			return;
		}

		ServletRequestAttributes attributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			String token = request.getHeader("Authorization");
			if (token != null) {
				requestTemplate.header("Authorization", token);
			}
		}
	}
}
