package com.fastline.vendorservice.infrastructure.swagger;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import com.fastline.vendorservice.presentation.response.order.OrderCreateResponse;
import com.fastline.vendorservice.presentation.response.order.OrderFindResponse;
import com.fastline.vendorservice.presentation.response.order.OrderStatusUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

public interface OrderControllerSwagger {

	@Operation(
			summary = "주문 생성",
			description =
					"""
						요청 객체에 맞게 주문을 생성합니다.
						- 배송 및 메세지 서비스와의 API통신이 포함되어 있으며, 모든 통신이 성공해야만 이 요청도 성공합니다.
						""",
			tags = {"주문"},
			requestBody =
					@io.swagger.v3.oas.annotations.parameters.RequestBody(
							description = "생성할 주문의 정보",
							required = true,
							content =
									@Content(
											mediaType = "application/json",
											schema = @Schema(implementation = OrderCreateRequest.class))))
	@PostMapping
	ResponseEntity<ApiResponse<OrderCreateResponse>> insertOrder(
			@RequestBody @Valid OrderCreateRequest createRequest,
			@AuthenticationPrincipal UserDetailsImpl userDetails);

	@Operation(
			summary = "주문 단건 조회",
			description = """
						orderId로 주문 단건을 조회합니다
						- 해당 ID로 주문이 존재하지 않는다면 예외 발생.
						""",
			tags = {"주문"},
			parameters = {
				@Parameter(
						name = "orderId",
						in = ParameterIn.QUERY,
						required = true,
						description = "조회할 주문의 ID",
						schema = @Schema(implementation = UUID.class))
			})
	@GetMapping
	ResponseEntity<ApiResponse<OrderFindResponse>> findOrder(
			@RequestParam UUID orderId, @AuthenticationPrincipal UserDetailsImpl userDetails);

	@Operation(
			summary = "주문 상태 수정",
			description =
					"""
						orderId에 해당하는 주문 상태를 status의 값으로 수정합니다.
						- 해당 Id로 주문이 존재하지 않는다면 예외 발생.
						- status 값이 서버내에서 유효하지 않다면 예외 발생.
						- status 값이 COMPLETED나 CANCELLED인 주문이라면 예외 발생.
						""",
			tags = {"주문"},
			parameters = {
				@Parameter(
						name = "orderId",
						in = ParameterIn.QUERY,
						required = true,
						description = "수정할 주문의 ID",
						schema = @Schema(implementation = UUID.class)),
				@Parameter(
						name = "status",
						in = ParameterIn.QUERY,
						required = true,
						description = "어떤 상태로 수정할지의 값",
						schema = @Schema(type = "string"))
			})
	@PutMapping
	ResponseEntity<ApiResponse<OrderStatusUpdateResponse>> updateStatus(
			@RequestParam UUID orderId,
			@RequestParam String status,
			@AuthenticationPrincipal UserDetailsImpl userDetails);

	@Operation(
			summary = "주문 단건 삭제",
			description =
					"""
						orderId로 주문 단건을 삭제합니다.
						- Soft Delete를 수행하며, DB에는 남아있습니다.
						""",
			tags = {"주문"},
			parameters = {
				@Parameter(
						name = "orderId",
						in = ParameterIn.QUERY,
						required = true,
						description = "삭제할 주문의 ID",
						schema = @Schema(implementation = UUID.class))
			})
	@DeleteMapping
	ResponseEntity<ApiResponse<UUID>> deletedOrder(
			@RequestParam UUID orderId, @AuthenticationPrincipal UserDetailsImpl userDetails);
}
