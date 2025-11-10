package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {}
