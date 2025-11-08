package com.fastline.authservice.domain.security;

import com.fastline.common.security.model.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

// DB의 회원정보 조회 -> Spring Security의 인증 관리자(UserDetials)에게 전달
//각 서비스마다 특별한 권한이 필요한 경우가 있으므로 각 서비스에 맞게 UserDetailsService를 구현
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	// pk는 userId인데 바꿀 수 없는지
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}
	public UserDetails loadUserInfo(Long userId, String username, String role, String hubId, String slackId) throws UsernameNotFoundException {
		UUID hubUUID = (hubId==null)? null:UUID.fromString(hubId);
        return new UserDetailsImpl(userId, username, "", role, hubUUID, slackId);
	}
}
