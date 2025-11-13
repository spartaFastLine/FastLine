package com.fastline.deliveryservice.application.service;

import com.fastline.deliveryservice.application.dto.HubRouteResult;
import java.util.List;
import java.util.UUID;

public interface HubClient {
	List<HubRouteResult> getRoutes(UUID fromHubId, UUID toHubId);
}
