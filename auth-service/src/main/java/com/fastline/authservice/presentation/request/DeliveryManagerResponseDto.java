package com.fastline.authservice.presentation.request;


import com.fastline.authservice.domain.model.DeliveryManager;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryManagerResponseDto {
    private final Long userId;
    private final UUID hubId;
    private final String type;
    private final Long number;

    public DeliveryManagerResponseDto(DeliveryManager deliveryManager) {
        this.userId = deliveryManager.getId();
        this.hubId = deliveryManager.getUser().getHubId();
        this.type = deliveryManager.getType().name();
        this.number = deliveryManager.getNumber();
    }
}
