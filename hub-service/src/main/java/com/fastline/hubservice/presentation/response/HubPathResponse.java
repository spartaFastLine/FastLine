package com.fastline.hubservice.presentation.response;
import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.domain.model.HubPath;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

/**
 * 허브 경로 단건 조회 응답 DTO
 */
public record HubPathResponse(
        UUID hubPathId,
        UUID startHubId,
        String startHubName,
        UUID endHubId,
        String endHubName,
        LocalTime duration,
        Long distance,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static HubPathResponse from(HubPath entity) {
        Hub start = entity.getStartHub();
        Hub end = entity.getEndHub();
        return new HubPathResponse(
                entity.getHubPathId(),
                start != null ? start.getHubId() : null,
                start != null ? start.getName() : null,
                end != null ? end.getHubId() : null,
                end != null ? end.getName() : null,
                entity.getDuration(),
                entity.getDistance(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }}
