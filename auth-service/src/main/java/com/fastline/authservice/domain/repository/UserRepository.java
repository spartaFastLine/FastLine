package com.fastline.authservice.domain.repository;

import com.fastline.authservice.domain.model.DeliveryManager;
import com.fastline.authservice.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
	Optional<User> findUserByEmail(String email);

	Optional<User> findUserByUsername(String userName);

	Optional<User> findUserById(Long id);

	Page<User> findUsers(String username, UUID hubId, String role, String status, Pageable pageable);

	void save(User user);

	Long countDeliveryManagers();

	Page<DeliveryManager> findDeliveryManagers(
			String username,
			UUID hubId,
			String deliveryType,
			Long number,
			String userStatus,
			boolean isActive,
			Pageable pageable);
}
