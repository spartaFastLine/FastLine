package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.application.service.HubClient;
import java.util.UUID;

import com.fastline.vendorservice.infrastructure.external.dto.HubResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {

	private final HubFeignClient hubFeignClient;

	@Override
	public HubResponseDto getHubInfo(UUID hubId) {
		return hubFeignClient.getHubInfo(hubId);
	}
}
