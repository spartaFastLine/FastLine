package com.fastline.authservice.domain.repository;

import com.fastline.authservice.domain.model.DeliveryManager;

import java.util.Optional;

public interface DeliveryManagerRepository {
    void save(DeliveryManager deliveryManager);
    long countAll();

    Optional<DeliveryManager> findById(Long userId);
}
