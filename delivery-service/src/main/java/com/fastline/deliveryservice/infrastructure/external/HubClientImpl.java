package com.fastline.deliveryservice.infrastructure.external;

import com.fastline.common.response.ApiResponse;
import com.fastline.deliveryservice.application.dto.HubRouteResult;
import com.fastline.deliveryservice.application.service.HubClient;
import com.fastline.deliveryservice.infrastructure.external.dto.HubRoutesResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {

	private final HubServiceFeignClient feignClient;

	@Override
	public List<HubRouteResult> getRoutes(UUID fromHubId, UUID toHubId) {
		ApiResponse<HubRoutesResponse> response = feignClient.getHubRoutes(fromHubId, toHubId);

		if (!response.isSuccess() || response.getData() == null) {
			throw new IllegalStateException("허브 간 경로 정보를 조회하지 못했습니다: " + response.getMessage());
		}

		return response.getData().routes().stream().map(HubRouteResult::from).toList();
	}
}
