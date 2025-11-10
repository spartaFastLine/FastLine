package com.fastline.authservice.domain.service;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.common.security.model.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckUser {
	private final UserRepository userRepository;

	public User userCheck(Long userId) {
		return userRepository
				.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	// 허브 매니저라면 소속 허브 아이디 체크
	public void checkHubManager(UserDetailsImpl manager, UUID requestHubId) {
		if (manager.getRole() == UserRole.HUB_MANAGER && !manager.getHubId().equals(requestHubId))
			throw new CustomException(ErrorCode.NOT_HUB_MANAGER);
	}
}
