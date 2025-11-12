package com.fastline.vendorservice.application.service;

import com.fastline.vendorservice.infrastructure.external.dto.UserResponseDto;

public interface UserClient {

    UserResponseDto getUserInfo(Long userId);
}
