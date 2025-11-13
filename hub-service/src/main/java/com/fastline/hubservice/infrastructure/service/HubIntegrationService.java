package com.fastline.hubservice.infrastructure.service;

import com.fastline.hubservice.infrastructure.DelivaryClient;
import com.fastline.hubservice.infrastructure.NaverApiClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubIntegrationService {

	private final NaverApiClient naverApiClient;
	private final DelivaryClient delivaryClient;

	public void callExternalApis() {
		var result =
				naverApiClient.getDrivingDirection("127.1054221,37.3591614", "127.1054221,37.3591614");
		System.out.println(result);

		var order = delivaryClient.getDelivery(UUID.fromString("abcd..."));
		System.out.println(order.getBody());
	}
}
