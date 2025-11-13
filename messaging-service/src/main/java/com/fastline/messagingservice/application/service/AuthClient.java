package com.fastline.messagingservice.application.service;

import com.fastline.messagingservice.application.dto.AuthResult;

public interface AuthClient {
	AuthResult loadInfo(Long deliveryManagerId);
}
