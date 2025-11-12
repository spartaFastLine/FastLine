package com.fastline.deliveryservice.infrastructure.external;

import com.fastline.common.response.ApiResponse;
import com.fastline.deliveryservice.infrastructure.external.dto.ManagerAssignResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceFeignClient {

	@GetMapping("/api/managers/assign/{hubId}/{managerType}")
	ApiResponse<ManagerAssignResponse> assignManager(
			@PathVariable UUID hubId, @PathVariable String managerType);

	@GetMapping("/api/deliveries/{managerId}/complete")
	ApiResponse<Void> deliveryComplete(@PathVariable Long managerId);
}
