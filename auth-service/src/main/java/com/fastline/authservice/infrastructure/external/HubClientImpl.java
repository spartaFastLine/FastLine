package com.fastline.authservice.infrastructure.external;

import com.fastline.authservice.application.result.HubExistResult;
import com.fastline.authservice.application.service.HubClient;
import com.fastline.authservice.infrastructure.external.dto.HubExistResponse;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.response.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {
	private final HubServiceFeignClient feignClient;

	@Override
	public HubExistResult getHubExists(UUID hubId) {
		ApiResponse<HubExistResponse> response = feignClient.getHubExists(hubId);
		if (!response.isSuccess() || response.getData() == null)
			throw new CustomException(ErrorCode.HUB_NOT_FOUND);
		return HubExistResult.from(response.getData());
	}
}
