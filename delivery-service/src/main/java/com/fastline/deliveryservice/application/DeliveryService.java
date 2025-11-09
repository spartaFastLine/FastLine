package com.fastline.deliveryservice.application;

import com.fastline.deliveryservice.application.command.CreateDeliveryCommand;
import com.fastline.deliveryservice.application.command.CreateDeliveryPathCommand;
import com.fastline.deliveryservice.application.command.UpdateDeliveryCommand;
import com.fastline.deliveryservice.application.dto.DeliveryResult;
import com.fastline.deliveryservice.domain.entity.Delivery;
import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import com.fastline.deliveryservice.domain.repository.DeliveryRepository;
import com.fastline.deliveryservice.domain.service.DeliveryDomainService;
import com.fastline.deliveryservice.domain.vo.OrderId;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryDomainService deliveryDomainService;

	@Transactional
	public UUID createDelivery(CreateDeliveryCommand command) {

		log.info("배송 생성 시작: orderId={}", command.orderId());

		OrderId orderId = OrderId.of(command.orderId());
		validateOrder(orderId);

		List<DeliveryPath> deliveryPaths = createDeliveryPaths(command.paths());

		Delivery delivery =
				Delivery.create(
						command.orderId(),
						command.vendorSenderId(),
						command.vendorReceiverId(),
						command.startHubId(),
						command.endHubId(),
						command.address(),
						command.recipientUsername(),
						command.recipientSlackId(),
						command.vendorDeliveryManagerId(),
						deliveryPaths);

		deliveryDomainService.validateDelivery(delivery);

		Delivery savedDelivery = deliveryRepository.save(delivery);

		log.info("배송 생성 완료: deliveryId={}", savedDelivery.getDeliveryId());

		return savedDelivery.getDeliveryId();
	}

	private void validateOrder(OrderId orderId) {
		//        if (!orderClient.exists(orderId)) throw new IllegalArgumentException("유효하지 않은 주문 ID");
		log.debug("주문 유효성 검증 완료: {}", orderId);
	}

	private List<DeliveryPath> createDeliveryPaths(List<CreateDeliveryPathCommand> pathCommands) {
		return pathCommands.stream()
				.map(
						cmd ->
								DeliveryPath.create(
										cmd.sequence(),
										cmd.fromHubId(),
										cmd.toHubId(),
										cmd.expDistance(),
										cmd.expDuration(),
										cmd.deliveryManagerId()))
				.toList();
	}

	@Transactional(readOnly = true)
	public DeliveryResult getDelivery(UUID deliveryId) {
		Delivery delivery =
				deliveryRepository
						.findById(deliveryId)
						.orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));

		return DeliveryResult.from(delivery);
	}

	@Transactional
	public void updateDelivery(UpdateDeliveryCommand command) {
		log.info("배송 정보 수정 시작: deliveryId={}", command.deliveryId());

		Delivery delivery =
				deliveryRepository
						.findById(command.deliveryId())
						.orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));

		delivery.changeStatus(command.status());
		delivery.updateDeliveryInfo(
				command.address(),
				command.recipientUsername(),
				command.recipientSlackId(),
				command.vendorDeliveryManagerId());
		delivery.updatePaths(command.paths());

		log.info("배송 정보 수정 완료: deliveryId={}", command.deliveryId());
	}

	@Transactional
	public void deleteDelivery(UUID deliveryId, Long userId) {
		log.info("배송 정보 삭제 시작: deliveryId={}", deliveryId);

		Delivery delivery =
				deliveryRepository
						.findById(deliveryId)
						.orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));

		delivery.delete(userId);

		log.info("배송 정보 삭제 완료: deliveryId={}", deliveryId);
	}
}
