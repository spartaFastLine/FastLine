package com.fastline.gatewayservice.security;

import com.fastline.common.security.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomReaciveUserDetailsServiceImpl implements CustomReactiveUserDetailsService {

	// gatesay service에서는 인가를 위한 role만 필요하므로 아래 메서드만 사용
	@Override
	public Mono<UserDetailsImpl> loadUserInfo(Claims claims) {
		Long userId = claims.get("userId", Long.class);
		String username = claims.get("sub", String.class);
		String role = claims.get("auth", String.class);
		return Mono.just(new UserDetailsImpl(userId, username, "", role, null, null));
	}
}
