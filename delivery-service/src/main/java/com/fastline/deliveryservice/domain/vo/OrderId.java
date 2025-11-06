package com.fastline.deliveryservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderId {

    private UUID id;

    private OrderId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("유효하지 않은 주문 ID입니다.");
        }
        this.id = id;
    }

    public static OrderId of(UUID id) {
        return new OrderId(id);
    }
}
