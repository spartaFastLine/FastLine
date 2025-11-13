package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.application.service.HubClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {

	private final HubFeignClient hubFeignClient;

	@Override
	public Boolean validateHubId(UUID hubId) {
		return hubFeignClient.checkHubId(hubId);
	}
}
