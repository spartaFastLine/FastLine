package com.fastline.deliveryservice.infrastructure.external.dto;

import java.util.UUID;

public record RouteInfoResponse(
		int sequence, UUID fromHubId, UUID toHubId, int expDistance, int expDuration, String hubName) {}
