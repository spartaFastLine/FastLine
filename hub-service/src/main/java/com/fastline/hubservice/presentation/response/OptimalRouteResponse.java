package com.fastline.hubservice.presentation.response;

import com.fastline.hubservice.domain.model.HubPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OptimalRouteResponse {
    private List<HubPathResponse> legs; // 구간 리스트
    private long totalDistance;         // meters
    private long totalDurationSec;      // seconds

    public static OptimalRouteResponse of(List<HubPath> legs, long dist, long sec) {
        return OptimalRouteResponse.builder()
                .legs(legs.stream().map(HubPathResponse::from).toList())
                .totalDistance(dist)
                .totalDurationSec(sec)
                .build();
    }
}
