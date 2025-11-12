package com.fastline.deliveryservice.application;

import com.fastline.deliveryservice.application.command.*;
import com.fastline.deliveryservice.application.dto.*;
import com.fastline.deliveryservice.application.service.AuthClient;
import com.fastline.deliveryservice.application.service.HubClient;
import com.fastline.deliveryservice.application.service.VendorClient;
import com.fastline.deliveryservice.domain.entity.Delivery;
import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import com.fastline.deliveryservice.domain.entity.DeliveryStatus;
import com.fastline.deliveryservice.domain.repository.DeliveryPathRepository;
import com.fastline.deliveryservice.domain.repository.DeliveryRepository;
import com.fastline.deliveryservice.domain.service.DeliveryDomainService;
import com.fastline.deliveryservice.domain.vo.OrderId;
import com.fastline.deliveryservice.presentation.dto.PageResponse;
import com.fastline.deliveryservice.presentation.dto.response.DeliveryPathSummaryResponse;
import com.fastline.deliveryservice.presentation.dto.response.DeliverySummaryResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;
    private final DeliveryPathRepository deliveryPathRepository;
	private final DeliveryDomainService deliveryDomainService;
    private final VendorClient vendorClient;
    private final HubClient hubClient;
    private final AuthClient authClient;

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

		Delivery delivery =
				deliveryRepository
						.findById(deliveryId)
						.orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));

		if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
			log.warn("이미 배송 완료 상태입니다. deliveryId={}", deliveryId);
			return;
		}

		delivery.completeIfAllPathsArrived();

		log.info("배송 완료 처리 완료: deliveryId={}", deliveryId);
	}

    @Transactional(readOnly = true)
    public List<DeliveryPathDetailResult> getPaths(GetDeliveryPathsCommand command) {
        Delivery delivery = deliveryRepository.findById(command.deliveryId())
                .orElseThrow(() -> new NotFoundException("해당 배송이 존재하지 않습니다."));

        return delivery.getPaths().stream()
                .map(DeliveryPathDetailResult::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeliveryPathDetailResult getPath(GetDeliveryPathCommand command) {
        Delivery delivery = deliveryRepository.findById(command.deliveryId())
                .orElseThrow(() -> new NotFoundException("해당 배송이 존재하지 않습니다."));

        DeliveryPath path = delivery.getPaths().stream()
                .filter(p -> p.getDeliveryPathId().equals(command.pathId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("해당 경로가 존재하지 않습니다."));

        return DeliveryPathDetailResult.from(path);
    }

    @Transactional(readOnly = true)
    public PageResponse<DeliveryPathSummaryResponse> searchDeliveryPaths(SearchDeliveryPathCommand command) {
        log.info("배송 경로 검색 시작: page={}, size={}, sortBy={}, direction={}",
                command.page(), command.size(), command.sortBy(), command.direction());

        Sort.Direction direction =
                Sort.Direction.fromOptionalString(command.direction()).orElse(Sort.Direction.DESC);

        Pageable pageable =
                PageRequest.of(command.page(), command.size(), Sort.by(direction, command.sortBy()));

        Page<DeliveryPath> paths = deliveryPathRepository.searchDeliveryPaths(pageable);

        log.info("배송 경로 검색 완료: page={}, totalElements={}", command.page(), paths.getTotalElements());
        return PageResponse.from(paths.map(DeliveryPathSummaryResponse::from));
    }

    @Transactional
    public DeliveryFromOrderCreateResult createDeliveryFromOrder(CreateDeliveryFromOrderCommand command) {
        log.info("주문 기반 배송 생성 시작: orderId={}", command.orderId());

        VendorInfoResult vendorInfo = vendorClient.getVendorInfo(command.vendorSenderId(), command.vendorReceiverId());

        List<HubRouteResult> routes = hubClient.getRoutes(vendorInfo.startHubId(), vendorInfo.endHubId());

        List<CreateDeliveryPathCommand> paths = routes.stream()
                .map(route -> {
                    ManagerAssignResult managerAssignResult = authClient.assign(route.fromHubId());
                    return new CreateDeliveryPathCommand(
                            route.sequence(),
                            route.fromHubId(),
                            route.toHubId(),
                            route.expDistance(),
                            route.expDuration(),
                            managerAssignResult.managerId()
                    );
                })
                .toList();

        Long vendorDeliveryManagerId = paths.stream()
                .max(Comparator.comparingInt(CreateDeliveryPathCommand::sequence))
                .map(CreateDeliveryPathCommand::deliveryManagerId)
                .orElse(null);

        CreateDeliveryCommand createCommand = new CreateDeliveryCommand(
                command.orderId(),
                command.vendorSenderId(),
                command.vendorReceiverId(),
                vendorInfo.startHubId(),
                vendorInfo.endHubId(),
                command.address(),
                command.recipientUsername(),
                command.recipientSlackId(),
                vendorDeliveryManagerId,
                paths
        );

        UUID deliveryId = createDelivery(createCommand);

        log.info("주문 기반 배송 생성 완료: orderId={}", command.orderId());

        return DeliveryFromOrderCreateResult.of(
                deliveryId,
                vendorDeliveryManagerId,
                routes.stream()
                        .sorted(Comparator.comparingInt(HubRouteResult::sequence))
                        .map(r -> r.fromHubId().toString())
                        .toList()
        );
    }
}
