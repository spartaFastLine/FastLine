package com.fastline.vendorservice.application.service;

import java.util.UUID;

public interface HubClient {

    Boolean validateHubId(UUID hubId);
}
