package com.fastline.vendorservice.application.service;

import com.fastline.vendorservice.infrastructure.external.dto.UserResponseDto;
import java.util.UUID;

public interface UserClient {

	UserResponseDto getUserInfo(Long userId);

	UUID getUserHubId(Long userId);
}
