package com.fastline.gatewayservice.filter;

import com.fastline.common.security.jwt.JwtUtil;
import com.fastline.gatewayservice.security.CustomReaciveUserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
	private final JwtUtil jwtUtil;
	private final CustomReaciveUserDetailsServiceImpl userDetailsService;

	@Override
	public @NonNull Mono<Void> filter(
			@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
		String token = resolveToken(exchange);
		if (token == null) return chain.filter(exchange); // 토큰이 없으면 인증필터 통과

		String rawToken = jwtUtil.substringToken(token); // Bearer 제거
		if (!jwtUtil.validateToken(rawToken)) return chain.filter(exchange); // 토큰이 유효하지 않으면 인증필터 통과

		// 토큰이 유효하면 사용자 정보 조회 및 인증 컨텍스트 설정
		Claims claims = jwtUtil.getUserInfoFromToken(rawToken);
		String username = claims.get("sub", String.class);
		if (username == null) return chain.filter(exchange); // username이 없으면 인증필터 통과

		return userDetailsService
				.loadUserInfo(claims)
				.flatMap(
						userDetails -> {
							Authentication auth =
									new UsernamePasswordAuthenticationToken(
											userDetails, null, userDetails.getAuthorities());
							SecurityContext securityContext = new SecurityContextImpl(auth);
							return chain
									.filter(exchange)
									.contextWrite(
											ReactiveSecurityContextHolder.withSecurityContext(
													Mono.just(securityContext)));
						})
				.switchIfEmpty(chain.filter(exchange)); // 사용자 정보 조회 실패 시 인증필터 통과
	}

	private String resolveToken(ServerWebExchange request) {
		String header = request.getRequest().getHeaders().getFirst(JwtUtil.AUTHORIZATION_HEADER);
		if (header != null && header.startsWith(JwtUtil.BEARER_PREFIX)) {
			return header;
		}
		return null;
	}
}
