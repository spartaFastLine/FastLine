package com.fastline.hubservice.infrastructure;

import com.fastline.hubservice.infrastructure.config.NaverFeignConfig;
import com.fastline.hubservice.infrastructure.dto.NaverDirectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "naverApiClient",
        url = "https://naveropenapi.apigw.ntruss.com",
        configuration = NaverFeignConfig.class
)
public interface NaverApiClient {

    @GetMapping("/map-direction/v1/driving")
    NaverDirectionResponse getDrivingDirection(
            @RequestParam("start") String start,
            @RequestParam("goal") String goal
    );
}
