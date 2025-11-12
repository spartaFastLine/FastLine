package com.fastline.deliveryservice.infrastructure.repository;

import com.fastline.deliveryservice.domain.entity.Delivery;
import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import com.fastline.deliveryservice.domain.repository.DeliveryPathRepository;
import com.fastline.deliveryservice.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeliveryRepositoryAdapter implements DeliveryRepository, DeliveryPathRepository {

	private final JpaDeliveryRepository jpaDeliveryRepository;
	private final JpaDeliveryPathRepository jpaDeliveryPathRepository;

	@Override
	public Delivery save(Delivery delivery) {
		return jpaDeliveryRepository.save(delivery);
	}

	@Override
	public Optional<Delivery> findById(UUID deliveryId) {
		return jpaDeliveryRepository.findById(deliveryId);
	}

	@Override
	public Page<Delivery> searchDeliveries(Pageable pageable) {
		return jpaDeliveryRepository.findByDeletedAtIsNull(pageable);
	}

    @Override
    public Page<DeliveryPath> searchDeliveryPaths(Pageable pageable) {
        return jpaDeliveryPathRepository.findByDeletedAtIsNull(pageable);
    }
}
