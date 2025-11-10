package com.fastline.deliveryservice.presentation;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.deliveryservice.application.DeliveryService;
import com.fastline.deliveryservice.application.command.CreateDeliveryCommand;
import com.fastline.deliveryservice.application.command.CreateDeliveryPathCommand;
import com.fastline.deliveryservice.application.command.UpdateDeliveryCommand;
import com.fastline.deliveryservice.application.command.UpdateDeliveryPathCommand;
import com.fastline.deliveryservice.presentation.dto.request.CreateDeliveryRequest;
import com.fastline.deliveryservice.presentation.dto.request.UpdateDeliveryRequest;
import com.fastline.deliveryservice.presentation.dto.response.DeliveryCreateResponse;
import com.fastline.deliveryservice.presentation.dto.response.DeliveryDetailResponse;
import com.fastline.deliveryservice.presentation.dto.response.DeliveryUpdateResponse;
import jakarta.validation.Valid;
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

	/* 배송 경로 기록 삭제 */
	@DeleteMapping("/{deliveryId}/paths/{pathId}")
	public ResponseEntity<ApiResponse<Void>> deleteDeliveryPath(
			@PathVariable UUID deliveryId, @PathVariable UUID pathId) {
		log.info("배송 경로 기록 삭제 요청: deliveryId={}, pathId={}", deliveryId, pathId);

		Long userId = 1234L; // 추후 인증 연동 예정

		deliveryService.deletePath(deliveryId, pathId, userId);

		log.info("배송 경로 기록 삭제 성공: deliveryId={}, pathId={}", deliveryId, pathId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_PATH_DELETE_SUCCESS);
	}
}
