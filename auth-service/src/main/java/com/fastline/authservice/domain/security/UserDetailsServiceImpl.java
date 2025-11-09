package com.fastline.authservice.domain.security;

import com.fastline.common.security.model.CustomUserDetailsService;
import com.fastline.common.security.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

// DB의 회원정보 조회 -> Spring Security의 인증 관리자(UserDetials)에게 전달
//각 서비스마다 특별한 권한이 필요한 경우가 있으므로 각 서비스에 맞게 UserDetailsService를 구현
@Slf4j
@Service
public class UserDetailsServiceImpl implements CustomUserDetailsService {

	// JWT 토큰의 Claims 정보를 이용하여 UserDetails 생성
	@Override
	public UserDetails loadUserInfo(Claims claims) throws UsernameNotFoundException {
		Long userId = claims.get("userId", Long.class);
		String username = claims.get("sub", String.class);
		String role = claims.get("auth", String.class);
		String hubId = claims.get("hubId", String.class);
//		String slackId = claims.get("slackId", String.class);  //slackId가 필요한 경우만 활성화 및 생성자에 입력
		UUID hubUUID = (hubId==null)? null:UUID.fromString(hubId);
		return new UserDetailsImpl(userId, username, "", role, hubUUID, null);
	}
}
