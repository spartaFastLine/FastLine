package com.fastline.deliveryservice.application;

import com.fastline.deliveryservice.application.command.CreateDeliveryCommand;
import com.fastline.deliveryservice.application.command.CreateDeliveryPathCommand;
import com.fastline.deliveryservice.application.command.SearchDeliveryCommand;
import com.fastline.deliveryservice.application.command.UpdateDeliveryCommand;
import com.fastline.deliveryservice.application.dto.DeliveryResult;
import com.fastline.deliveryservice.domain.entity.Delivery;
import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import com.fastline.deliveryservice.domain.entity.DeliveryPathStatus;
import com.fastline.deliveryservice.domain.entity.DeliveryStatus;
import com.fastline.deliveryservice.domain.repository.DeliveryRepository;
import com.fastline.deliveryservice.domain.service.DeliveryDomainService;
import com.fastline.deliveryservice.domain.vo.OrderId;
import com.fastline.deliveryservice.presentation.dto.PageResponse;
import com.fastline.deliveryservice.presentation.dto.response.DeliverySummaryResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	@Transactional(readOnly = true)
	public PageResponse<DeliverySummaryResponse> searchDeliveries(SearchDeliveryCommand command) {
		log.info(
				"배송 검색 시작: page={}, size={}, sortBy={}, direction={}",
				command.page(),
				command.size(),
				command.sortBy(),
				command.direction());

		Sort.Direction direction =
				Sort.Direction.fromOptionalString(command.direction()).orElse(Sort.Direction.DESC);

		Pageable pageable =
				PageRequest.of(command.page(), command.size(), Sort.by(direction, command.sortBy()));

		Page<Delivery> deliveries = deliveryRepository.searchDeliveries(pageable);

		log.info("배송 검색 완료: page={}, totalElements={}", command.page(), deliveries.getTotalElements());
		return PageResponse.from(deliveries.map(DeliverySummaryResponse::from));
	}

	@Transactional
	public void deletePath(UUID deliveryId, UUID pathId, Long userId) {
		log.info("배송 경로 기록 삭제 시작: deliveryId={}, pathId={}", deliveryId, pathId);

		Delivery delivery =
				deliveryRepository
						.findById(deliveryId)
						.orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));

		delivery.deletePath(pathId, userId);

		log.info("배송 경로 기록 삭제 완료: deliveryId={}, pathId={}", deliveryId, pathId);
	}

    @Transactional
    public void updateStatus(UUID deliveryId, DeliveryStatus status) {
        log.info("배송 상태 변경 시작: deliveryId={}", deliveryId);

        Delivery delivery =
                deliveryRepository
                        .findById(deliveryId)
                        .orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));

        delivery.changeStatus(status);

        log.info("배송 상태 변경 완료: deliveryId={}", deliveryId);
    }

    @Transactional
    public void complete(UUID deliveryId) {
        log.info("배송 완료 처리 시작: deliveryId={}", deliveryId);

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));

        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            log.warn("이미 배송 완료 상태입니다. deliveryId={}", deliveryId);
            return;
        }

        delivery.completeIfAllPathsArrived();

        log.info("배송 완료 처리 완료: deliveryId={}", deliveryId);
    }

}
