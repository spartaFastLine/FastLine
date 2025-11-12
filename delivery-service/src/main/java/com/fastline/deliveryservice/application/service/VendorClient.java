package com.fastline.deliveryservice.application.service;

import com.fastline.deliveryservice.application.dto.VendorInfoResult;
import java.util.UUID;

public interface VendorClient {
	VendorInfoResult getVendorInfo(UUID vendorSenderId, UUID vendorReceiverId);
}
