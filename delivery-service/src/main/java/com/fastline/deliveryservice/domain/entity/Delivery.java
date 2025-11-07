package com.fastline.deliveryservice.domain.entity;

import com.fastline.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "delivery_id")
	private UUID deliveryId;

	@Column(name = "order_id", nullable = false)
	private UUID orderId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private DeliveryStatus status;

	@Column(name = "vendor_sender_id", nullable = false)
	private UUID vendorSenderId;

	@Column(name = "vendor_receiver_id", nullable = false)
	private UUID vendorReceiverId;

	@Column(name = "start_hub_id", nullable = false)
	private UUID startHubId;

	@Column(name = "end_hub_id", nullable = false)
	private UUID endHubId;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "recipient_username", nullable = false)
	private String recipientUsername;

	@Column(name = "recipient_slack_id", nullable = false)
	private String recipientSlackId;

	@Column(name = "vendor_delivery_manager_id", nullable = false)
	private Long vendorDeliveryManagerId;

	@OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DeliveryPath> paths = new ArrayList<>();

	public static Delivery create(
			UUID orderId,
			UUID vendorSenderId,
			UUID vendorReceiverId,
			UUID startHubId,
			UUID endHubId,
			String address,
			String recipientUsername,
			String recipientSlackId,
			Long vendorDeliveryManagerId,
			List<DeliveryPath> paths) {

		validateDeliveryPaths(paths);

		Delivery delivery = new Delivery();
		delivery.status = DeliveryStatus.DELIVERY_PENDING;
		delivery.orderId = orderId;
		delivery.vendorSenderId = vendorSenderId;
		delivery.vendorReceiverId = vendorReceiverId;
		delivery.startHubId = startHubId;
		delivery.endHubId = endHubId;
		delivery.address = address;
		delivery.recipientUsername = recipientUsername;
		delivery.recipientSlackId = recipientSlackId;
		delivery.vendorDeliveryManagerId = vendorDeliveryManagerId;

		for (DeliveryPath path : paths) {
			delivery.addPath(path);
		}

		return delivery;
	}

	public void addPath(DeliveryPath path) {
		this.paths.add(path);
		path.setDelivery(this);
	}

	public void startDelivery() {
		if (status != DeliveryStatus.DELIVERY_PENDING)
			throw new IllegalStateException("대기 상태에서만 배송을 시작할 수 있습니다.");
		status = DeliveryStatus.DELIVERY_TRANSIT;
	}

	public void completeDelivery() {
		if (status != DeliveryStatus.DELIVERY_TRANSIT)
			throw new IllegalStateException("배송 중 상태에서만 완료할 수 있습니다.");
		status = DeliveryStatus.DELIVERED;
	}

	private static void validateDeliveryPaths(List<DeliveryPath> paths) {
		if (paths == null || paths.isEmpty()) {
			throw new IllegalArgumentException("배달 경로는 최소 1개 이상이어야 합니다");
		}
	}
}
