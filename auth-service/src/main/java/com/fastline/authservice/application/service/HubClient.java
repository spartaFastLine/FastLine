package com.fastline.authservice.application.service;

import com.fastline.authservice.application.result.HubExistResult;

import java.util.UUID;


public interface HubClient {
	HubExistResult getHubExists(UUID hubId);
}
