package com.fastline.vendorservice.application.service;

import com.fastline.vendorservice.infrastructure.external.dto.HubResponseDto;
import java.util.UUID;

public interface HubClient {

	HubResponseDto getHubInfo(UUID hubId);
}
