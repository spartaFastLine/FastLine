package com.fastline.deliveryservice.infrastructure.repository;

import com.fastline.deliveryservice.domain.entity.Delivery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {
	Page<Delivery> findByDeletedAtIsNull(Pageable pageable);
}
