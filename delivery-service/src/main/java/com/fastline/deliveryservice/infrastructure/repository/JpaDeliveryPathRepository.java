package com.fastline.deliveryservice.infrastructure.repository;

import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeliveryPathRepository extends JpaRepository<DeliveryPath, UUID> {
	Page<DeliveryPath> findByDeletedAtIsNull(Pageable pageable);
}
