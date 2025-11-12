package com.fastline.deliveryservice.infrastructure.external;

import com.fastline.common.response.ApiResponse;
import com.fastline.deliveryservice.application.dto.VendorInfoResult;
import com.fastline.deliveryservice.application.service.VendorClient;
import com.fastline.deliveryservice.infrastructure.external.dto.OrderDeliveryCompleteRequest;
import com.fastline.deliveryservice.infrastructure.external.dto.VendorInfoResponse;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VendorClientImpl implements VendorClient {

	private final VendorServiceFeignClient feignClient;

	@Override
	public VendorInfoResult getVendorInfo(UUID vendorSenderId, UUID vendorReceiverId) {
		ApiResponse<VendorInfoResponse> response =
				feignClient.getVendorInfo(vendorSenderId, vendorReceiverId);

		if (!response.isSuccess() || response.getData() == null) {
			throw new IllegalStateException("업체 정보를 조회하지 못했습니다: " + response.getMessage());
		}

		return VendorInfoResult.from(response.getData());
	}

	@Override
	public void deliveryComplete(UUID orderId, Instant arrivedAt) {
		OrderDeliveryCompleteRequest request = new OrderDeliveryCompleteRequest(arrivedAt);

		ApiResponse<Void> response = feignClient.deliveryComplete(orderId, request);

		if (!response.isSuccess()) {
			throw new IllegalStateException("주문 도착 반영 실패: " + response.getMessage());
		}
	}
}
