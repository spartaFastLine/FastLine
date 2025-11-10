package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

	@Query("SELECT o from Order o JOIN fetch o.orderProducts where o.id = :orderId")
	Optional<Order> findByOrderIdFetchJoin(@Param("orderId") UUID orderId);
}
