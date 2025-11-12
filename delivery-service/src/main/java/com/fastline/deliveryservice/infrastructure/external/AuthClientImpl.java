package com.fastline.deliveryservice.infrastructure.external;

import com.fastline.common.response.ApiResponse;
import com.fastline.deliveryservice.application.dto.ManagerAssignResult;
import com.fastline.deliveryservice.application.service.AuthClient;
import com.fastline.deliveryservice.infrastructure.external.dto.ManagerAssignResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthClientImpl implements AuthClient {

	private final AuthServiceFeignClient feignClient;

	@Override
	public ManagerAssignResult assign(UUID hubId, String managerType) {
		ApiResponse<ManagerAssignResponse> response = feignClient.assignManager(hubId, managerType);

		if (!response.isSuccess() || response.getData() == null) {
			throw new IllegalStateException("배송 담당자 자동 배정 실패: " + response.getMessage());
		}

		return ManagerAssignResult.from(response.getData());
	}

    @Override
    public void deliveryComplete(Long managerId) {
        ApiResponse<Void> response = feignClient.deliveryComplete(managerId);

        if (!response.isSuccess()) {
            throw new IllegalStateException("배송 담당자 완료 알림 실패: " + response.getMessage());
        }
    }
}
