package com.fastline.vendorservice.infrastructure.external.dto;

import java.time.Instant;
import java.util.UUID;

public record HubResponseDto(
		UUID hubId,
		UUID centralHubId,
		boolean isCentral,
		String name,
		String address,
		Double latitude,
		Double longitude,
		Instant createdAt,
		Instant updatedAt) {}
