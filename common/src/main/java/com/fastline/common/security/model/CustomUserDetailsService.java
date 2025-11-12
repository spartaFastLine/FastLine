package com.fastline.common.security.model;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// DB의 회원정보 조회 -> Spring Security의 인증 관리자(UserDetials)에게 전달
// 각 서비스마다 특별한 권한이 필요한 경우가 있으므로 각 서비스에 맞게 UserDetailsService를 구현
@Slf4j
@Service
public class CustomUserDetailsService {

	// JWT 토큰의 Claims 정보를 이용하여 UserDetails 생성
	public UserDetails loadUserInfo(Claims claims) throws UsernameNotFoundException {
		Long userId = Long.valueOf(claims.get("sub", String.class));
		String role = claims.get("auth", String.class);
		return new UserDetailsImpl(userId, "", "", role);
	}
}
