package com.fastline.authservice.infrastructure.configuration;

import com.fastline.common.exception.CustomAccessDeniedHandler;
import com.fastline.common.exception.CustomAuthenticationEntryPoint;
import com.fastline.common.security.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthorizationFilter jwtFilter;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 비활성화 : 사용자가 로그인한 상태에서 의도치 않게 특정 웹사이트에 악성 요청을 보내도록 유도하는 웹 보안 공격을 막음
		http.csrf(csrf -> csrf.disable());

		// filter에서 권한 체크
		http.authorizeHttpRequests(
				authorizeHttpRequests ->
						authorizeHttpRequests
								.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
								.permitAll() // resources 접근 허용 설정
								.requestMatchers("/api/auth/signup", "/api/auth/login")
								.permitAll() // 회원가입, 로그인 접근 허용
								.anyRequest()
								.authenticated()); // 그 외 모든 요청 인증처리
		// 예외 처리 핸들러 등록
		http.exceptionHandling(
				ex ->
						ex.authenticationEntryPoint(customAuthenticationEntryPoint)
								.accessDeniedHandler(customAccessDeniedHandler));

		// jwt(토큰 기반 인증 방식)는 세션을 필요로 하지 않음, STATELESS -> 완전 사용 안함
		http.sessionManagement(
				session ->
						session.sessionCreationPolicy(
								org.springframework.security.config.http.SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
