package com.fastline.authservice.infrastructure.external;

import com.fastline.authservice.infrastructure.external.dto.HubExistResponse;
import com.fastline.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("hub-service")
public interface HubServiceFeignClient {

    @GetMapping("/api/hubs/{hubId}/exists")
    ApiResponse<HubExistResponse> getHubExists(@PathVariable("hubId") UUID hubId);
}