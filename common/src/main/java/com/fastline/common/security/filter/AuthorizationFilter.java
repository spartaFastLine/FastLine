package com.fastline.common.security.filter;

import com.fastline.common.security.jwt.JwtUtil;
import com.fastline.common.security.model.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// 모든 요청에 대해 JWT 토큰의 유효성을 검사하는 필터
// 기본적으로 활성화, application.yml에서 security.authorization-filter.enabled: false로 설정 시 비활성화
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
		name = "security.authorization-filter.enabled",
		havingValue = "true",
		matchIfMissing = true)
public class AuthorizationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		// 특정 경로에 대해서는 필터를 적용하지 않음 (예: 로그인, 회원가입 등)
		return path.startsWith("/api/auth/") || path.startsWith("/actuator");
	}

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		try {
			logger.debug("URI:" + request.getRequestURL());
			System.out.println("URI:" + request.getRequestURL());
			String token = jwtUtil.resolveToken(request);
			if (token == null) filterChain.doFilter(request, response); // 토큰이 없으면 인증필터 통과

			String rawToken = jwtUtil.substringToken(token); // Bearer 제거
			if (!jwtUtil.validateToken(rawToken))
				filterChain.doFilter(request, response); // 토큰이 유효하지 않으면 인증필터 통과

			// 토큰이 유효하면 사용자 정보 조회 및 인증 컨텍스트 설정
			Claims claims = jwtUtil.getUserInfoFromToken(rawToken);
			String userId = claims.get("sub", String.class);
			if (userId == null) filterChain.doFilter(request, response); // username이 없으면 인증필터 통

			// 사용자 정보 조회 - UserDetailsServiceImpl 각 서비스에 필요한 권한에 따라 커스텀 구현
			var userDetails = userDetailsService.loadUserInfo(claims);
			logger.debug("uerDetails : " + userDetails.toString());
			Authentication auth =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception ex) {
			// 인증 실패해도 요청은 다음 필터로 넘겨져서 정상적으로 401/403 처리됨.
			logger.error(ex.getMessage());
		}
		filterChain.doFilter(request, response);
	}
}
