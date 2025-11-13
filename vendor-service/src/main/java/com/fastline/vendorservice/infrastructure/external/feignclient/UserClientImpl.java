package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.application.service.UserClient;
import com.fastline.vendorservice.infrastructure.external.dto.UserResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

	private final UserFeignClient userFeignClient;

	@Override
	public UserResponseDto getUserInfo(Long userId) {
		return userFeignClient.getUserInfo(userId);
	}

	@Override
	public UUID getUserHubId(Long userId) {
		return userFeignClient.getUserHubId(userId);
	}
}
