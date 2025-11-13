package com.fastline.deliveryservice.application.service;

import com.fastline.deliveryservice.application.dto.VendorInfoResult;
import java.time.Instant;
import java.util.UUID;

public interface VendorClient {
	VendorInfoResult getVendorInfo(UUID vendorSenderId, UUID vendorReceiverId);

	void deliveryComplete(UUID orderId, Instant arrivedAt);
}
