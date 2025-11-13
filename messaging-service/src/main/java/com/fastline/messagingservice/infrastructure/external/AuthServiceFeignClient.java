package com.fastline.messagingservice.infrastructure.external;

import com.fastline.messagingservice.infrastructure.external.dto.response.DeliveryManagerInfoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceFeignClient {
	@GetMapping("/api/users/{userId}")
	DeliveryManagerInfoResponseDTO loadInfo(@PathVariable Long userId);
}
