package com.fastline.deliveryservice.infrastructure.external;

import com.fastline.common.response.ApiResponse;
import com.fastline.deliveryservice.infrastructure.external.dto.OrderDeliveryCompleteRequest;
import com.fastline.deliveryservice.infrastructure.external.dto.VendorInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "vendor-service")
public interface VendorServiceFeignClient {

	@GetMapping("/api/vendors/info")
	ApiResponse<VendorInfoResponse> getVendorInfo(
			@RequestParam UUID vendorSenderId, @RequestParam UUID vendorReceiverId);

    @PatchMapping("/api/orders/{orderId}/delivery-complete")
    ApiResponse<Void> deliveryComplete(
            @PathVariable UUID orderId,
            @RequestBody OrderDeliveryCompleteRequest request
    );
}
