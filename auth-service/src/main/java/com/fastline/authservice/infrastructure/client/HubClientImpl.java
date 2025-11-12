package com.fastline.authservice.infrastructure.client;

import com.fastline.authservice.application.service.HubClient;
import com.fastline.authservice.presentation.dto.request.HubExistRequest;

import java.util.UUID;

public class HubClientImpl implements HubClient {
    @Override
    public HubExistRequest getHubExists(UUID hubId) {
        return null;
    }
}
