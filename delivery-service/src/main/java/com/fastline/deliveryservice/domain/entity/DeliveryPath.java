package com.fastline.deliveryservice.domain.entity;

import com.fastline.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_paths")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryPath extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "delivery_path_id")
	private UUID deliveryPathId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id", nullable = false)
	private Delivery delivery;

	@Column(name = "sequence", nullable = false)
	private int sequence;

	@Column(name = "from_hub_id", nullable = false)
	private UUID fromHubId;

	@Column(name = "to_hub_id", nullable = false)
	private UUID toHubId;

	@Column(name = "exp_distance", nullable = false)
	private int expDistance;

	@Column(name = "act_distance")
	private int actDistance;

	@Column(name = "exp_duration", nullable = false)
	private int expDuration;

	@Column(name = "act_duration")
	private int actDuration;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private DeliveryPathStatus status;

	@Column(name = "delivery_manager_id", nullable = false)
	private Long deliveryManagerId;

	public static DeliveryPath create(
			int sequence,
			UUID fromHubId,
			UUID toHubId,
			int expDistance,
			int expDuration,
			Long deliveryManagerId) {
		DeliveryPath path = new DeliveryPath();
		path.sequence = sequence;
		path.fromHubId = fromHubId;
		path.toHubId = toHubId;
		path.expDistance = expDistance;
		path.expDuration = expDuration;
		path.deliveryManagerId = deliveryManagerId;
		path.status = DeliveryPathStatus.HUB_PENDING;
		return path;
	}

	public void startTransit() {
		if (status != DeliveryPathStatus.HUB_PENDING)
			throw new IllegalStateException("허브 대기 상태에서만 이동 시작이 가능합니다.");
		status = DeliveryPathStatus.HUB_TRANSIT;
	}

	public void arriveHub() {
		if (status != DeliveryPathStatus.HUB_TRANSIT)
			throw new IllegalStateException("이동 중 상태에서만 도착할 수 있습니다.");
		status = DeliveryPathStatus.HUB_ARRIVED;
	}

	protected void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
}
