package com.fastline.hubservice.presentation.response;

import com.fastline.hubservice.domain.model.Hub;
import java.time.Instant;
import java.util.UUID;

/** 허브 목록 응답 DTO */
public record HubListResponse(
		UUID hubId,
		UUID centralHubId,
		boolean isCentral,
		String name,
		String address,
		Double latitude,
		Double longitude,
		Instant createdAt) {
	public static HubListResponse from(Hub hub) {
		return new HubListResponse(
				hub.getHubId(),
				hub.getCentralHubId(),
				hub.isCentral(),
				hub.getName(),
				hub.getAddress(),
				hub.getLatitude(),
				hub.getLongitude(),
				hub.getCreatedAt());
	}
}
