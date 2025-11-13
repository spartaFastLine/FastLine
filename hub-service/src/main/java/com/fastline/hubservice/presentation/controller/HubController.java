package com.fastline.hubservice.presentation.controller;

import com.fastline.hubservice.application.command.CreateHubCommand;
import com.fastline.hubservice.application.command.HubSearchCommand;
import com.fastline.hubservice.application.service.HubService;
import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.presentation.ApisResponse;
import com.fastline.hubservice.presentation.request.CreateHubRequest;
import com.fastline.hubservice.presentation.request.HubListRequest;
import com.fastline.hubservice.presentation.response.HubCreateResponse;
import com.fastline.hubservice.presentation.response.HubGetResponse;
import com.fastline.hubservice.presentation.response.HubListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Hub", description = "허브 관리 API")
public class HubController {

	private final HubService hubService;

	/** 허브 생성 POST /api/hubs */
	@Operation(summary = "허브 생성", description = "허브를 생성합니다")
	@io.swagger.v3.oas.annotations.responses.ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "201",
				description = "생성 성공",
				content = @Content(schema = @Schema(implementation = HubCreateResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description = "유효성 오류"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "409",
				description = "중복 충돌")
	})
	@PostMapping({""})
	public ResponseEntity<ApisResponse<HubCreateResponse>> CreateHub(
			@Valid @RequestBody CreateHubRequest request) {

		CreateHubCommand command =
				new CreateHubCommand(
						request.getCentralHubId(),
						request.getIsCentral(),
						request.getName(),
						request.getAddress(),
						request.getLatitude(),
						request.getLongitude());

		// 2. 애플리케이션 서비스 호출
		UUID hubId = hubService.createHub(command);

		// 3. 응답 생성
		HubCreateResponse response = new HubCreateResponse(hubId, "허브가 성공적으로 생성되었습니다");
		log.info("허브 생성 성공: hub_id={}", hubId);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApisResponse.success(response));
	}

	/** 허브 단건 조회 GET /api/hub/{hubId} */
	@Operation(summary = "허브 단건 조회", description = "hubId로 허브를 조회합니다")
	@io.swagger.v3.oas.annotations.responses.ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "조회 성공",
				content = @Content(schema = @Schema(implementation = HubGetResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "404",
				description = "허브 없음")
	})
	@GetMapping("/{hubId}")
	public ResponseEntity<ApisResponse<HubGetResponse>> getHub(@PathVariable("hubId") UUID hubId) {
		Hub hub = hubService.getHub(hubId); // 애플리케이션 서비스에서 조회 (미구현 시 추가 필요)
		HubGetResponse response = HubGetResponse.from(hub);
		return ResponseEntity.ok(ApisResponse.success(response));
	}

	/**
	 * 허브 다건 조회 (필터 + 페이지네이션) GET /api/hubs
	 *
	 * <p>Query Params: - name: 부분 일치 검색 - address: 부분 일치 검색 - centralHubId: 상위(중앙) 허브 ID - isCentral:
	 * 중앙 허브 여부 - page, size, sort: 스프링 기본 페이지네이션 파라미터
	 */
	@GetMapping({""})
	@Operation(summary = "허브 목록 조회", description = "필터와 페이지네이션으로 허브 목록을 조회합니다")
	@io.swagger.v3.oas.annotations.responses.ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "조회 성공")
	})
	public ResponseEntity<ApisResponse<Page<HubListResponse>>> listHubs(
			@ParameterObject @org.springframework.web.bind.annotation.ModelAttribute
					HubListRequest request,
			@ParameterObject
					@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
					Pageable pageable) {

		HubSearchCommand command =
				new HubSearchCommand(
						request.getName(),
						request.getAddress(),
						request.getCentralHubId(),
						request.getIsCentral());
		Page<Hub> page = hubService.searchHubs(command, pageable);
		if (page == null) {
			page = Page.empty(pageable);
		}
		Page<HubListResponse> body = page.map(HubListResponse::from);
		return ResponseEntity.ok(ApisResponse.success(body));
	}

	/**
	 * 허브 소프트 삭제 DELETE /api/hub/{hubId}
	 *
	 * <p>허브를 실제로 삭제하지 않고 deletedAt 컬럼에 시간 값을 세팅합니다.
	 */
	@DeleteMapping("/{hubId}")
	@Operation(summary = "허브 소프트 삭제", description = "허브를 소프트 삭제합니다 (deletedAt 설정)")
	@io.swagger.v3.oas.annotations.responses.ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "삭제 완료"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "404",
				description = "허브 없음")
	})
	public ResponseEntity<ApisResponse<String>> deleteHub(@PathVariable("hubId") UUID hubId) {
		hubService.softDeleteHub(hubId);
		log.info("허브 소프트 삭제 완료: hub_id={}", hubId);
		return ResponseEntity.ok(ApisResponse.success("허브가 소프트 삭제되었습니다."));
	}
}
