package com.fastline.deliveryservice.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "p_deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private UUID deliveryId;
}
