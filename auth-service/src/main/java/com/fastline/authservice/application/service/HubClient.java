package com.fastline.authservice.application.service;

import com.fastline.authservice.presentation.dto.request.HubExistRequest;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("hub-service")
public interface HubClient {
	@GetMapping("/api/hubs/{hubId}/exists")
    HubExistRequest getHubExists(@PathVariable("hubId") UUID hubId);
}
