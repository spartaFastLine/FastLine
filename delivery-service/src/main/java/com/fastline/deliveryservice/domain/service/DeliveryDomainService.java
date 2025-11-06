package com.fastline.deliveryservice.domain.service;

import com.fastline.deliveryservice.domain.entity.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryDomainService {

    public void validateDelivery(Delivery delivery) {
        if (delivery.getPaths().isEmpty()) {
            throw new IllegalArgumentException("배송 경로가 존재하지 않습니다.");
        }

        var firstPath = delivery.getPaths().get(0);
        if (!firstPath.getFromHubId().equals(delivery.getStartHubId())) {
            throw new IllegalStateException("첫 배송 경로의 출발 허브가 배송 시작 허브와 일치하지 않습니다.");
        }

        var lastPath = delivery.getPaths().get(delivery.getPaths().size() - 1);
        if (!lastPath.getToHubId().equals(delivery.getEndHubId())) {
            throw new IllegalStateException("마지막 배송 경로의 도착 허브가 배송 도착 허브와 일치하지 않습니다.");
        }

        log.debug("배송 경로 일관성 검증 완료: deliveryId={}", delivery.getDeliveryId());
    }
}
