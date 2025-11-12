package com.fastline.authservice.domain.service;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.infrastructure.client.HubClient;
import com.fastline.authservice.presentation.dto.request.HubExistRequestDto;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.model.UserRole;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckUser {
	private final UserRepository userRepository;
	private final HubClient hubClient;

	public User userCheck(Long userId) {
		return userRepository
				.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	// 허브 매니저라면 소속 허브 아이디 체크
	public void checkHubManager(User manager, UUID requestHubId) {
		if (manager.getRole() == UserRole.HUB_MANAGER && !manager.getHubId().equals(requestHubId))
			throw new CustomException(ErrorCode.NOT_HUB_MANAGER);
	}

	public void checkHubExist(@NotNull UUID hubId) {
		HubExistRequestDto hubExist = hubClient.getHubExists(hubId);
		if (!hubExist.isExist()) {
			throw new CustomException(ErrorCode.HUB_NOT_FOUND);
		}
	}
}
