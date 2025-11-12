package com.fastline.deliveryservice.application.service;

import com.fastline.deliveryservice.application.dto.ManagerAssignResult;
import java.util.UUID;

public interface AuthClient {
	ManagerAssignResult assign(UUID hubId, String managerType);
}
