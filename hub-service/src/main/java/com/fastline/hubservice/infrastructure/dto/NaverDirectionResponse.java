package com.fastline.hubservice.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Naver Map Direction API Response
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverDirectionResponse {

    private String code;
    private String message;
    private Route route;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Route {
        private List<Traoptimal> traoptimal;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Traoptimal {
        private Summary summary;
        private List<List<Double>> path;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Double distance;     // meters
        private Integer duration;    // milliseconds
        private Start start;
        private Goal goal;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Start {
        private Double lat;
        private Double lng;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Goal {
        private Double lat;
        private Double lng;
    }
}
