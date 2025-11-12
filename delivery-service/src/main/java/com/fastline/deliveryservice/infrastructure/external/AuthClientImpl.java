package com.fastline.deliveryservice.infrastructure.external;

import com.fastline.common.response.ApiResponse;
import com.fastline.deliveryservice.application.dto.ManagerAssignResult;
import com.fastline.deliveryservice.application.service.AuthClient;
import com.fastline.deliveryservice.infrastructure.external.dto.ManagerAssignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthClientImpl implements AuthClient {

    private final AuthServiceFeignClient feignClient;

    @Override
    public ManagerAssignResult assign(UUID hubId) {
        ApiResponse<ManagerAssignResponse> response = feignClient.assignManager(hubId);

        if (!response.isSuccess() || response.getData() == null) {
            throw new IllegalStateException("배송 담당자 자동 배정 실패: " + response.getMessage());
        }

        return ManagerAssignResult.from(response.getData());
    }
}
