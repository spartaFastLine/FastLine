package com.fastline.gatewayservice.config;

import com.fastline.common.security.filter.AuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<AuthorizationFilter> correlationIdFilterRegistration(
			AuthorizationFilter authFilter) {
		FilterRegistrationBean<AuthorizationFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(authFilter);
		reg.addUrlPatterns("/api/*"); // 모든 URL 패턴에 대해 필터 적용
		reg.setOrder(1); // 필터의 우선순위 설정
		return reg;
	}
}
