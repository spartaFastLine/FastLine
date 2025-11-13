package com.fastline.vendorservice.infrastructure.swagger;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.vendorservice.presentation.request.ProductCreateRequest;
import com.fastline.vendorservice.presentation.request.ProductUpdateRequest;
import com.fastline.vendorservice.presentation.response.product.ProductCreateResponse;
import com.fastline.vendorservice.presentation.response.product.ProductFindResponse;
import com.fastline.vendorservice.presentation.response.product.ProductUpdateResponse;
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

public interface ProductControllerSwagger {

	@Operation(
			summary = "상품 생성",
			description = """
						요청 객체에 맞게 상품을 생성합니다.
						- 업체의 ID가 유효하지 않다면 예외 발생.
						""",
			tags = "상품",
			requestBody =
					@io.swagger.v3.oas.annotations.parameters.RequestBody(
							description = "생성할 상품의 정보",
							required = true,
							content =
									@Content(
											mediaType = "application/json",
											schema = @Schema(implementation = ProductCreateRequest.class))))
	@PostMapping
	ResponseEntity<ApiResponse<ProductCreateResponse>> insertProduct(
			@RequestBody @Valid ProductCreateRequest createRequest,
			@AuthenticationPrincipal UserDetailsImpl userDetails);

	@Operation(
			summary = "상품 단건 조회",
			description =
					"""
												productId로 상품 단건을 조회합니다
												- 해당 Id로 상품이 존재하지 않는다면 예외 발생.
												""",
			tags = {"상품"},
			parameters = {
				@Parameter(
						name = "productId",
						in = ParameterIn.QUERY,
						required = true,
						description = "조회할 상품의 ID",
						schema = @Schema(implementation = UUID.class))
			})
	@GetMapping
	ResponseEntity<ApiResponse<ProductFindResponse>> findProduct(@RequestParam UUID productId);

	@Operation(
			summary = "상품 정보 수정",
			description =
					"""
												productId에 해당하는 상품의 정보를 주어진 객체에 맞게 수정합니다.
												- 해당 Id로 상품이 존재하지 않는다면 예외 발생.
												- 주어진 객체에 존재하는 값들만 변경.
												- 주어진 객체에 값들이 전부 없다면 예외 발생
												""",
			tags = {"상품"},
			parameters =
					@Parameter(
							name = "productId",
							in = ParameterIn.QUERY,
							required = true,
							description = "수정할 상품의 ID",
							schema = @Schema(implementation = UUID.class)),
			requestBody =
					@io.swagger.v3.oas.annotations.parameters.RequestBody(
							description = "상품이 해당하는 값으로 수정됨",
							required = true,
							content =
									@Content(
											mediaType = "application/json",
											schema = @Schema(implementation = ProductUpdateRequest.class))))
	@PutMapping
	ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(
			@RequestBody @Valid ProductUpdateRequest updateRequest,
			@RequestParam UUID productId,
			@AuthenticationPrincipal UserDetailsImpl userDetails);

	@Operation(
			summary = "상품 단건 삭제",
			description =
					"""
												productId로 주문 단건을 삭제합니다.
												- Soft Delete를 수행하며, DB에는 남아있습니다.
												""",
			tags = {"상품"},
			parameters = {
				@Parameter(
						name = "productId",
						in = ParameterIn.QUERY,
						required = true,
						description = "삭제할 상품의 ID",
						schema = @Schema(implementation = UUID.class))
			})
	@DeleteMapping
	ResponseEntity<ApiResponse<UUID>> deleteProduct(
			@RequestParam UUID productId, @AuthenticationPrincipal UserDetailsImpl userDetails);
}
