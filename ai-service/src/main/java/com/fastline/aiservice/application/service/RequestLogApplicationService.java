package com.fastline.aiservice.application.service;

import static com.fastline.common.exception.ErrorCode.GENERATION_FAIL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastline.aiservice.application.command.GenerateMessageCommand;
import com.fastline.aiservice.application.dto.MessageGenerationResult;
import com.fastline.aiservice.domain.entity.RequestLog;
import com.fastline.aiservice.domain.repository.RequestLogRepository;
import com.fastline.aiservice.domain.service.RequestLogDomainService;
import com.fastline.aiservice.infrastructure.gemini.GeminiClient;
import com.fastline.aiservice.infrastructure.gemini.dto.GeminiRequest;
import com.fastline.aiservice.infrastructure.gemini.dto.GeminiResponse;
import com.fastline.common.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestLogApplicationService {

	private final RequestLogRepository requestLogRepository;
	private final GeminiClient geminiClient;
	private final RequestLogDomainService requestLogDomainService;
	private final ObjectMapper objectMapper;

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.model}")
	private String model;

	@Transactional
	public MessageGenerationResult generateMessage(GenerateMessageCommand command) {

		RequestLog requestLog = RequestLog.ofRequested(command.orderId());
		requestLogRepository.save(requestLog);

		String commandString = objectMapper.valueToTree(command).toString();
		String prompt = buildPrompt(commandString);

		try {
			GeminiRequest requestBody =
					new GeminiRequest(
							List.of(new GeminiRequest.Content(List.of(new GeminiRequest.Part(prompt)))));

			GeminiResponse result = geminiClient.generate(requestBody, model, apiKey);
			String raw = result.candidates().get(0).content().parts().get(0).text();
			String sanitized = requestLogDomainService.sanitizeLLM(raw);

			if (!requestLogDomainService.isValidJson(sanitized)) {
				requestLog.fail(prompt, sanitized);
				throw new CustomException(GENERATION_FAIL);
			}

			MessageGenerationResult response =
					objectMapper.readValue(sanitized, MessageGenerationResult.class);
			requestLog.success(prompt, sanitized);

			return response;

		} catch (Exception e) {
			requestLog.fail(prompt, null);
			throw new CustomException(GENERATION_FAIL);
		}
	}

	private String buildPrompt(String orderInfoString) {
		return """
								아래 주문 정보를 고려하여, 해당 주문이 납기 요청을 맞추기 위해
								최종 발송되어야 하는 데드라인 시각을 산출해줘.

								### 요구사항
								- 요청자가 요구한 도착 납기시점 + 배송 담당자 근무시간(09 ~ 18) 을 고려할 것.
								- 경유지(센터) 이동 시간을 적절히 합리적으로 추정해서 반영할 것.
								- 결과 응답은 아래의 JSON 오브젝트 1개만 포함:
								{
									"finalDispatchDeadline": "위 내용을 기반으로 도출된 최종 발송 시한은 MM월 DD일 오전/오후 HH시 입니다."
								}
								- 응답은 반드시 JSON Object 1개만 출력하고, 어떤 코드 block, backtick, 설명, prefix 도 출력하지 말 것.
								- JSON Object 외의 어떠한 텍스트도 추가하지 말 것.

								### 주문 정보
								%s

								위 JSON Object 그대로만 출력해줘.
								"""
				.formatted(orderInfoString);
	}
}
