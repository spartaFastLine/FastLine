package com.fastline.authservice.application.result;

import com.fastline.authservice.infrastructure.external.dto.HubExistResponse;

public record HubExistResult(Boolean exist) {
	public static HubExistResult from(HubExistResponse response) {
		return new HubExistResult(response.exists());
	}
}
