package com.fastline.vendorservice.infrastructure.swagger;

import com.fastline.common.response.ApiResponse;
import com.fastline.vendorservice.presentation.request.VendorCreateRequest;
import com.fastline.vendorservice.presentation.request.VendorUpdateRequest;
import com.fastline.vendorservice.presentation.response.vendor.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface VendorControllerSwagger {

	@Operation(
			summary = "업체 생성",
			description =
					"""
						요청 객체에 맞게 업체를 생성합니다.
						- 배송 서비스에 배송ID가 유효한지를 확인하는 API통신이 포함되어 있으며, 통신이 성공해야만 이 요청도 성공합니다.
						""",
			tags = {"업체"},
			requestBody =
					@io.swagger.v3.oas.annotations.parameters.RequestBody(
							description = "생성할 업체의 정보",
							required = true,
							content =
									@Content(
											mediaType = "application/json",
											schema = @Schema(implementation = VendorCreateRequest.class))))
	@PostMapping
	ResponseEntity<ApiResponse<VendorCreateResponse>> insertVendor(
			@RequestBody @Valid VendorCreateRequest createRequest);

	@Operation(
			summary = "업체 단건 조회",
			description = """
						vendorId로 업체 하나를 조회합니다.
						- 해당 ID로 업체가 존재하지 않는다면 예외 발생.
						""",
			tags = {"업체"},
			parameters = {
				@Parameter(
						name = "vendorId",
						in = ParameterIn.QUERY,
						required = true,
						description = "조회할 업체의 ID",
						schema = @Schema(implementation = UUID.class))
			})
	@GetMapping
	ResponseEntity<ApiResponse<VendorFindResponse>> getVendor(@RequestParam UUID vendorId);

	@Operation(
			summary = "업체의 상품들 조회",
			description =
					"""
						vendorId의 업체가 생산한 상품들을 조회합니다.
						- 해당 vendorID로 업체가 존재하지 않는다면 예외 발생.
						- 해당 업체에 상품이 존재하지 않는다면 빈 리스트 반환.
						- 기본적으로 상품의 ID를 기준으로 오름차순 정렬.
						- 페이지 크기를 설정하지 않는다면, 10개씩 가져옴.
						""",
			tags = {"업체"},
			parameters = {
				@Parameter(
						name = "vendorId",
						in = ParameterIn.QUERY,
						required = true,
						description = "조회할 업체의 ID",
						schema = @Schema(implementation = UUID.class)),
				@Parameter(
						name = "page",
						in = ParameterIn.QUERY,
						required = false,
						description = "페이지의 시작점, 0부터 시작",
						schema = @Schema(implementation = Integer.class)),
				@Parameter(
						name = "size",
						in = ParameterIn.QUERY,
						required = false,
						description = "페이지의 크기",
						schema = @Schema(implementation = Integer.class)),
				@Parameter(
						name = "sort",
						in = ParameterIn.QUERY,
						required = false,
						description = "정렬 조건. 기본 타입이나 vo타입으로 정렬가능하고, 여러개 사용 가능",
						schema = @Schema(implementation = Object.class)),
			})
	@GetMapping("/{vendorId}/products")
	ResponseEntity<ApiResponse<VendorProductResponse>> getVendorProducts(
			@PathVariable UUID vendorId,
			@PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable);

	@Operation(
			summary = "업체의 주문들 조회",
			description =
					"""
						vendorId의 업체가 주문한 주문들을 조회합니다.
						- 해당 vendorID로 업체가 존재하지 않는다면 예외 발생.
						- 해당 업체의 주문내역이 존재하지 않는다면 빈 리스트 반환.
						- 판매시, 구매시 주문을 가리지 않고 모두 조회.
						- 기본적으로 판매할때의 업체ID를 기준으로 오름차순으로 정렬.
						- 페이지 크기를 설정하지 않는다면, 10개씩 가져옴.
						""",
			tags = {"업체"},
			parameters = {
				@Parameter(
						name = "vendorId",
						in = ParameterIn.QUERY,
						required = true,
						description = "조회할 업체의 ID",
						schema = @Schema(implementation = UUID.class)),
				@Parameter(
						name = "page",
						in = ParameterIn.QUERY,
						required = false,
						description = "페이지의 시작점, 0부터 시작",
						schema = @Schema(implementation = Integer.class)),
				@Parameter(
						name = "size",
						in = ParameterIn.QUERY,
						required = false,
						description = "페이지의 크기",
						schema = @Schema(implementation = Integer.class)),
				@Parameter(
						name = "sort",
						in = ParameterIn.QUERY,
						required = false,
						description = "정렬 조건. 기본 타입이나 vo타입으로 정렬가능하고, 여러개 사용 가능",
						schema = @Schema(implementation = Object.class)),
			})
	@GetMapping("/{vendorId}/orders")
	ResponseEntity<ApiResponse<VendorOrderResponse>> getVendorOrders(
			@PathVariable UUID vendorId,
			@PageableDefault(direction = Sort.Direction.ASC, sort = "vendorProducerId")
					Pageable pageable);

	@Operation(
			summary = "업체 정보 수정",
			description =
					"""
												vendorId에 해당하는 업체의 정보를 주어진 객체에 맞게 수정합니다.
												- 해당 Id로 업체가 존재하지 않는다면 예외 발생.
												- 주어진 객체에 존재하는 값들만 변경.
												- 주어진 객체에 값들이 전부 없다면 예외 발생
												""",
			tags = {"업체"},
			parameters = {
				@Parameter(
						name = "vendorId",
						in = ParameterIn.QUERY,
						required = true,
						description = "수정할 업체의 ID",
						schema = @Schema(implementation = UUID.class)),
			},
			requestBody =
					@io.swagger.v3.oas.annotations.parameters.RequestBody(
							description = "업체 정보가 해당하는 값으로 수정됨",
							required = true,
							content =
									@Content(
											mediaType = "application/json",
											schema = @Schema(implementation = VendorUpdateRequest.class))))
	@PutMapping
	ResponseEntity<ApiResponse<VendorUpdateResponse>> updateVendor(
			@RequestBody @Valid VendorUpdateRequest updateRequest, @RequestParam UUID vendorId);

	@Operation(
			summary = "업체 단건 삭제",
			description =
					"""
												vendorId로 업체 하나를 삭제합니다.
												- Soft Delete를 수행하며, DB에는 남아있습니다.
												""",
			tags = {"업체"},
			parameters = {
				@Parameter(
						name = "vendorId",
						in = ParameterIn.QUERY,
						required = true,
						description = "삭제할 업체의 ID",
						schema = @Schema(implementation = UUID.class))
			})
	@DeleteMapping
	ResponseEntity<ApiResponse<UUID>> deleteVendor(@RequestParam UUID vendorId);
}
