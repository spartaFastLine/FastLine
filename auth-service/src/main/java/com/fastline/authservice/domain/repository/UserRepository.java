package com.fastline.authservice.domain.repository;

import com.fastline.authservice.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String userName);
    Optional<User> findById(Long id);
    Page<User> findUsers(String username, UUID hubId, String role, String status, Pageable pageable);
    void save(User user);
}
