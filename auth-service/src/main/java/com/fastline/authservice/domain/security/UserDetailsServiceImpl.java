package com.fastline.authservice.domain.security;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserStatus;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// DB의 회원정보 조회 -> Spring Security의 인증 관리자(UserDetials)에게 전달
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	// pk는 userId인데 바꿀 수 없는지
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =
				userRepository
						.findByUsername(username)
						.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (user.getStatus() != UserStatus.APPROVE)
			throw new CustomException(ErrorCode.USER_NOT_APPROVE);
		return new UserDetailsImpl(user);
	}
}
