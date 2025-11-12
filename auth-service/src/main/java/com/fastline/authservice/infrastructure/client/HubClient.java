package com.fastline.authservice.infrastructure.client;

import com.fastline.authservice.presentation.dto.request.HubExistRequestDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("hub-service")
public interface HubClient {
	@GetMapping("/api/hubs/{hubId}/exists")
	HubExistRequestDto getHubExists(@PathVariable("hubId") UUID hubId);
}
