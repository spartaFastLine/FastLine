package com.fastline.authservice.infrastructure.repository;

import com.fastline.authservice.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);
	@Query("SELECT COUNT(u) FROM User u WHERE u.deliveryManager IS NOT NULL")
	Long countDeliveryManagers();
}
