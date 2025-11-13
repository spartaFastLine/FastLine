package com.fastline.vendorservice.infrastructure.external.feignclient;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

	@GetMapping("/api/hubs/{hubId}/exists")
	boolean checkHubId(@PathVariable UUID hubId);
}
