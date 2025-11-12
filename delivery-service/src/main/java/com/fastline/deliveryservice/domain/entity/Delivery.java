package com.fastline.deliveryservice.domain.entity;

import com.fastline.common.auditing.TimeBaseEntity;
import com.fastline.deliveryservice.application.command.UpdateDeliveryPathCommand;
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
public class Delivery extends TimeBaseEntity<Delivery> {

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

	/* 경로 기록 추가 */
	public void addPath(DeliveryPath path) {
		this.paths.add(path);
		path.setDelivery(this);
	}

	/* 배송 정보 수정 */
	public void updateDeliveryInfo(
			String address,
			String recipientUsername,
			String recipientSlackId,
			Long vendorDeliveryManagerId) {

		if (address != null) this.address = address;
		if (recipientUsername != null) this.recipientUsername = recipientUsername;
		if (recipientSlackId != null) this.recipientSlackId = recipientSlackId;
		if (vendorDeliveryManagerId != null) this.vendorDeliveryManagerId = vendorDeliveryManagerId;
	}

	/* 배송 상태 변경 */
	public void changeStatus(DeliveryStatus newStatus) {
		if (newStatus == null || this.status == newStatus) return;

		switch (newStatus) {
			case DELIVERY_TRANSIT -> startDelivery();
			case DELIVERED -> completeDelivery();
			default -> this.status = newStatus;
		}
	}

	public void startDelivery() {
		if (status != DeliveryStatus.DELIVERY_PENDING)
			throw new IllegalStateException("대기 상태에서만 배송을 시작할 수 있습니다.");
		this.status = DeliveryStatus.DELIVERY_TRANSIT;
	}

	public void completeDelivery() {
		if (status != DeliveryStatus.DELIVERY_TRANSIT)
			throw new IllegalStateException("배송 중 상태에서만 완료할 수 있습니다.");
		this.status = DeliveryStatus.DELIVERED;
	}

	private static void validateDeliveryPaths(List<DeliveryPath> paths) {
		if (paths == null || paths.isEmpty()) {
			throw new IllegalArgumentException("배달 경로는 최소 1개 이상이어야 합니다");
		}
	}

	/* 경로 기록 전체 수정 */
	public void updatePaths(List<UpdateDeliveryPathCommand> commands) {
		if (commands == null || commands.isEmpty()) return;

		for (UpdateDeliveryPathCommand cmd : commands) {
			DeliveryPath targetPath =
					this.paths.stream()
							.filter(p -> p.getSequence() == cmd.sequence())
							.findFirst()
							.orElseThrow(
									() ->
											new IllegalArgumentException("해당 시퀀스의 배송 경로를 찾을 수 없습니다: " + cmd.sequence()));

			targetPath.update(cmd.actDistance(), cmd.actDuration(), cmd.deliveryManagerId());

			if (cmd.status() != null) {
				targetPath.changeStatus(cmd.status());
			}
		}

		checkAndCompleteIfAllPathsArrived();
	}

	/* 모든 경로가 도착 -> 배송 완료 처리 */
	private void checkAndCompleteIfAllPathsArrived() {
		boolean allArrived =
				paths.stream().allMatch(p -> p.getStatus() == DeliveryPathStatus.HUB_ARRIVED);

		if (allArrived && this.status == DeliveryStatus.DELIVERY_TRANSIT) {
			completeDelivery();
		}
	}

    public void completeIfAllPathsArrived() {
        checkAndCompleteIfAllPathsArrived();
    }

	public void delete(Long userId) {
		this.markDeleted();
		//        this.deletedBy = userId;

		for (DeliveryPath path : this.paths) {
			path.delete(userId);
		}
	}

	public void deletePath(UUID pathId, Long userId) {
		DeliveryPath targetPath =
				this.paths.stream()
						.filter(p -> p.getDeliveryPathId().equals(pathId))
						.findFirst()
						.orElseThrow(() -> new IllegalArgumentException("해당 시퀀스의 배송 경로를 찾을 수 없습니다: " + pathId));

		targetPath.delete(userId);
	}
}
