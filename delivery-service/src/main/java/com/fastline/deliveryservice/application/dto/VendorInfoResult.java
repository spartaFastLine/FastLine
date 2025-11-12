package com.fastline.deliveryservice.application.dto;

import com.fastline.deliveryservice.infrastructure.external.dto.VendorInfoResponse;
import java.util.UUID;

public record VendorInfoResult(UUID startHubId, UUID endHubId) {
	public static VendorInfoResult from(VendorInfoResponse response) {
		return new VendorInfoResult(response.startHubId(), response.endHubId());
	}
}
