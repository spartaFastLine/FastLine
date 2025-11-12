package com.fastline.deliveryservice.domain.repository;

import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryPathRepository {
    Page<DeliveryPath> searchDeliveryPaths(Pageable pageable);
}
