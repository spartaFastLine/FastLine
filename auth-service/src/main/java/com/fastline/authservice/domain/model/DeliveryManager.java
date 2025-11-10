package com.fastline.authservice.domain.model;

import com.fastline.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_delivery_manager")
@NoArgsConstructor
public class DeliveryManager extends TimeBaseEntity {
	@Id
	@Column(name = "manager_id")
	private Long id;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "manager_id")
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeliveryManagerType type; // 배송 매니저 타입(허브 배달인, 업체 배달인)

	// 배송 순번
	private Long number;

	public DeliveryManager(User user, DeliveryManagerType type, long number) {
		this.user = user;
		this.type = type;
		this.number = number;
	}

	public void updateType(DeliveryManagerType type) {
		this.type = type;
	}

	public void delete() {
		markDeleted();
	}
}
