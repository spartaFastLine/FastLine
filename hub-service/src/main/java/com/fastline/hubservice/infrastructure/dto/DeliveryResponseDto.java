package com.fastline.hubservice.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for receiving delivery information from the delivery-service via Feign.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {

    private UUID deliveryId;
    private UUID orderId;
    private UUID startHubId;
    private UUID endHubId;
    private String status;
    private Double distance;
    private Integer duration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
