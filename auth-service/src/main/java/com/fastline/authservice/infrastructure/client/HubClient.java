package com.fastline.authservice.infrastructure.client;

import com.fastline.authservice.presentation.request.HubExistResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("hub-service")
public interface HubClient {
    @GetMapping("/api/hubs/{hubId}/exists")
    HubExistResponseDto getHubExists(@PathVariable("hubId") UUID hubId);
}
