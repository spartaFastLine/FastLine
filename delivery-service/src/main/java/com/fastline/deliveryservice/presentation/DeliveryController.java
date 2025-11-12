package com.fastline.deliveryservice.presentation;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.deliveryservice.application.DeliveryService;
import com.fastline.deliveryservice.application.command.*;
import com.fastline.deliveryservice.application.dto.DeliveryFromOrderCreateResult;
import com.fastline.deliveryservice.presentation.dto.PageResponse;
import com.fastline.deliveryservice.presentation.dto.request.CreateDeliveryFromOrderRequest;
import com.fastline.deliveryservice.presentation.dto.request.CreateDeliveryRequest;
import com.fastline.deliveryservice.presentation.dto.request.UpdateDeliveryRequest;
import com.fastline.deliveryservice.presentation.dto.request.UpdateDeliveryStatusRequest;
import com.fastline.deliveryservice.presentation.dto.response.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/deliverys")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	/* 배송 등록 */
	@PostMapping
	public ResponseEntity<ApiResponse<DeliveryCreateResponse>> createDelivery(
			@Valid @RequestBody CreateDeliveryRequest request) {
		log.info("배송 등록 요청: orderId={}", request.orderId());

		CreateDeliveryCommand command =
				new CreateDeliveryCommand(
						request.orderId(),
						request.vendorSenderId(),
						request.vendorReceiverId(),
						request.startHubId(),
						request.endHubId(),
						request.address(),
						request.recipientUsername(),
						request.recipientSlackId(),
						request.vendorDeliveryManagerId(),
						request.paths().stream()
								.map(
										p ->
												new CreateDeliveryPathCommand(
														p.sequence(),
														p.fromHubId(),
														p.toHubId(),
														p.expDistance(),
														p.expDuration(),
														p.deliveryManagerId()))
								.toList());

		UUID deliveryId = deliveryService.createDelivery(command);

		DeliveryCreateResponse response = new DeliveryCreateResponse(deliveryId);

		return ResponseUtil.successResponse(SuccessCode.DELIVERY_SAVE_SUCCESS, response);
	}

	/* 배송 조회 */
	@GetMapping("/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryDetailResponse>> getDelivery(
			@PathVariable UUID deliveryId) {
		log.info("배송 조회 요청: deliveryId={}", deliveryId);

		DeliveryDetailResponse response =
				DeliveryDetailResponse.from(deliveryService.getDelivery(deliveryId));

		log.info("배송 조회 성공: deliveryId={}", deliveryId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_FIND_SUCCESS, response);
	}

	/* 배송 수정 */
	@PutMapping("/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryUpdateResponse>> updateDelivery(
			@PathVariable UUID deliveryId, @Valid @RequestBody UpdateDeliveryRequest request) {
		log.info("배송 수정 요청: deliveryId={}", deliveryId);

		UpdateDeliveryCommand command =
				new UpdateDeliveryCommand(
						deliveryId,
						request.status(),
						request.address(),
						request.recipientUsername(),
						request.recipientSlackId(),
						request.vendorDeliveryManagerId(),
						request.paths() != null
								? request.paths().stream()
										.map(
												p ->
														new UpdateDeliveryPathCommand(
																p.sequence(),
																p.actDistance(),
																p.actDuration(),
																p.status(),
																p.deliveryManagerId()))
										.toList()
								: null);

		deliveryService.updateDelivery(command);

		DeliveryUpdateResponse response = new DeliveryUpdateResponse(deliveryId);

		log.info("배송 수정 성공: deliveryId={}", deliveryId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_UPDATE_SUCCESS, response);
	}

	/* 배송 삭제 */
	@DeleteMapping("/{deliveryId}")
	public ResponseEntity<ApiResponse<Void>> deleteDelivery(@PathVariable UUID deliveryId) {
		log.info("배송 삭제 요청: deliveryId={}", deliveryId);

		Long userId = 1234L; // 추후 로그인 사용자 정보 가져오게 수정 필요

		deliveryService.deleteDelivery(deliveryId, userId);

		log.info("배송 삭제 성공: deliveryId={}", deliveryId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_DELETE_SUCCESS);
	}

	/* 배송 검색 */
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<DeliverySummaryResponse>>> searchDeliveries(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") String direction) {
		log.info("배송 검색 요청: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

		if (size != 10 && size != 30 && size != 50) size = 10;

		SearchDeliveryCommand command = new SearchDeliveryCommand(page, size, sortBy, direction);
		PageResponse<DeliverySummaryResponse> response = deliveryService.searchDeliveries(command);

		log.info("배송 검색 성공: page={}, totalElements={}", page, response.totalElements());
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_SEARCH_SUCCESS, response);
	}

	/* 배송 상태 변경 */
	@PatchMapping("/{deliveryId}/status")
	public ResponseEntity<ApiResponse<DeliveryStatusUpdateResponse>> updateStatus(
			@PathVariable UUID deliveryId, @Valid @RequestBody UpdateDeliveryStatusRequest request) {
		log.info("배송 상태 변경 요청: deliveryId={}", deliveryId);

		deliveryService.updateStatus(deliveryId, request.status());

		log.info("배송 상태 변경 성공: deliveryId={}", deliveryId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_STATUS_UPDATE_SUCCESS);
	}

	/* 배송 완료 처리 */
	@PostMapping("/{deliveryId}/complete")
	public ResponseEntity<ApiResponse<Void>> completeDelivery(@PathVariable UUID deliveryId) {
		log.info("배송 완료 처리 요청: deliveryId={}", deliveryId);

		deliveryService.complete(deliveryId);

		log.info("배송 완료 처리 성공: deliveryId={}", deliveryId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_COMPLETE_SUCCESS);
	}

    /* 배송별 경로 전체 조회 */
    @GetMapping("/{deliveryId}/paths")
    public ResponseEntity<ApiResponse<List<DeliveryPathDetailResponse>>> getPaths(@PathVariable UUID deliveryId) {
        log.info("배송별 경로 전체 조회 요청: deliveryId={}", deliveryId);

        GetDeliveryPathsCommand command = new GetDeliveryPathsCommand(deliveryId);

        List<DeliveryPathDetailResponse> response = deliveryService.getPaths(command).stream().map(DeliveryPathDetailResponse::from).toList();

        log.info("배송별 경로 전체 조회 성공: deliveryId={}, pathCount={}", deliveryId, response.size());
        return ResponseUtil.successResponse(SuccessCode.DELIVERY_PATHS_FIND_SUCCESS, response);
    }

    /* 단일 경로 기록 조회 */
    @GetMapping("/{deliveryId}/paths/{pathId}")
    public ResponseEntity<ApiResponse<DeliveryPathDetailResponse>> getPath(
            @PathVariable UUID deliveryId,
            @PathVariable UUID pathId) {
        log.info("단일 경로 조회 요청: deliveryId={}, pathId={}", deliveryId, pathId);

        GetDeliveryPathCommand command = new GetDeliveryPathCommand(deliveryId, pathId);

        DeliveryPathDetailResponse response =
                DeliveryPathDetailResponse.from(deliveryService.getPath(command));

        log.info("단일 경로 조회 성공: deliveryId={}, pathId={}", deliveryId, pathId);
        return ResponseUtil.successResponse(SuccessCode.DELIVERY_PATH_FIND_SUCCESS, response);
    }

    /* 배송 경로 기록 검색 */
    @GetMapping("/paths")
    public ResponseEntity<ApiResponse<PageResponse<DeliveryPathSummaryResponse>>> searchDeliveryPaths(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        log.info("배송 경로 검색 요청: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

        if (size != 10 && size != 30 && size != 50) size = 10;

        SearchDeliveryPathCommand command = new SearchDeliveryPathCommand(page, size, sortBy, direction);

        PageResponse<DeliveryPathSummaryResponse> response = deliveryService.searchDeliveryPaths(command);

        log.info("배송 경로 검색 성공: page={}, totalElements={}", page, response.totalElements());
        return ResponseUtil.successResponse(SuccessCode.DELIVERY_PATH_SEARCH_SUCCESS, response);
    }

    /* 주문 생성 시 배송 생성 (내부 통신 api) */
    @PostMapping("/from-order")
    public ResponseEntity<ApiResponse<DeliveryFromOrderCreateResponse>> createDeliveryFromOrder(
            @Valid @RequestBody CreateDeliveryFromOrderRequest request) {
        log.info("주문 기반 배송 생성 요청: orderId={}", request.orderId());

        CreateDeliveryFromOrderCommand command = new CreateDeliveryFromOrderCommand(
                request.orderId(),
                request.vendorSenderId(),
                request.vendorReceiverId(),
                request.recipientUsername(),
                request.recipientSlackId(),
                request.address()
        );

        DeliveryFromOrderCreateResult result = deliveryService.createDeliveryFromOrder(command);

        DeliveryFromOrderCreateResponse response = DeliveryFromOrderCreateResponse.from(result);

        log.info("주문 기반 배송 생성 성공: orderId={}", request.orderId());
        return ResponseUtil.successResponse(SuccessCode.DELIVERY_FROM_ORDER_SAVE_SUCCESS, response);
    }
}
