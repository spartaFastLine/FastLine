package com.fastline.hubservice.presentation.response;

import com.fastline.hubservice.domain.model.Hub;
import java.time.Instant;
import java.util.UUID;

public final class HubGetResponse {
	private final UUID hubId;
	private final UUID centralHubId;
	private final boolean isCentral;
	private final String name;
	private final String address;
	private final Double latitude;
	private final Double longitude;
	private final Instant createdAt;
	private final Instant updatedAt;

	private HubGetResponse(
			UUID hubId,
			UUID centralHubId,
			boolean isCentral,
			String name,
			String address,
			Double latitude,
			Double longitude,
			Instant createdAt,
			Instant updatedAt) {
		this.hubId = hubId;
		this.centralHubId = centralHubId;
		this.isCentral = isCentral;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static HubGetResponse from(Hub hub) {
		return new HubGetResponse(
				hub.getHubId(),
				hub.getCentralHubId(),
				hub.isCentral(),
				hub.getName(),
				hub.getAddress(),
				hub.getLatitude(),
				hub.getLongitude(),
				hub.getCreatedAt(),
				hub.getUpdatedAt());
	}

	public UUID getHubId() {
		return hubId;
	}

	public UUID getCentralHubId() {
		return centralHubId;
	}

	public boolean isCentral() {
		return isCentral;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}
