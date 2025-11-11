package com.fastline.deliveryservice.domain.repository;

import com.fastline.deliveryservice.domain.entity.Delivery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {

	Delivery save(Delivery delivery);

	Optional<Delivery> findById(UUID deliveryId);

	Page<Delivery> searchDeliveries(Pageable pageable);
}
