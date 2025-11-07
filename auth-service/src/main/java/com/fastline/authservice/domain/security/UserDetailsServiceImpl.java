package com.fastline.authservice.domain.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// DB의 회원정보 조회 -> Spring Security의 인증 관리자(UserDetials)에게 전달
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	// pk는 userId인데 바꿀 수 없는지
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}
	// pk는 userId인데 바꿀 수 없는지
	public UserDetails loadUserInfo(Long userId, String username, String role) throws UsernameNotFoundException {
		return new UserDetailsImpl(userId, username, "", role);
	}
}
