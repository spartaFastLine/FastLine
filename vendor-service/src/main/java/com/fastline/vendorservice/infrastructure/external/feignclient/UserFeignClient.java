package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.infrastructure.external.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface UserFeignClient {

    @GetMapping("/api/users/{userId}")
    UserResponseDto getUserInfo(@PathVariable Long userId);
}
