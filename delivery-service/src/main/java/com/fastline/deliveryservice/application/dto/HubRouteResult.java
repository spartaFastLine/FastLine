package com.fastline.deliveryservice.application.dto;

import com.fastline.deliveryservice.infrastructure.external.dto.RouteInfoResponse;
import java.util.UUID;

public record HubRouteResult(
		int sequence, UUID fromHubId, UUID toHubId, int expDistance, int expDuration, String hubName) {
	public static HubRouteResult from(RouteInfoResponse response) {
		return new HubRouteResult(
				response.sequence(),
				response.fromHubId(),
				response.toHubId(),
				response.expDistance(),
				response.expDuration(),
				response.hubName());
	}
}
