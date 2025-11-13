package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.infrastructure.external.dto.HubResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

	@GetMapping("/api/hubs/{hubId}/exists")
	HubResponseDto getHubInfo(@PathVariable UUID hubId);
}
