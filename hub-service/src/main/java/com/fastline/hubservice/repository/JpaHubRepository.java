package com.fastline.hubservice.repository;

import com.fastline.hubservice.domain.model.Hub;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {}
