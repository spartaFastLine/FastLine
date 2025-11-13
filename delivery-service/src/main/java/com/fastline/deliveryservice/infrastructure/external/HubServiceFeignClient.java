package com.fastline.deliveryservice.infrastructure.external;

import com.fastline.common.response.ApiResponse;
import com.fastline.deliveryservice.infrastructure.external.dto.HubRoutesResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service")
public interface HubServiceFeignClient {

	@GetMapping("/api/hubs/routes")
	ApiResponse<HubRoutesResponse> getHubRoutes(
			@RequestParam UUID fromHubId, @RequestParam UUID toHubId);
}
