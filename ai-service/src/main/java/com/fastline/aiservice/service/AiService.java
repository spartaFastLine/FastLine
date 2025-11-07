package com.fastline.aiservice.service;

import static com.fastline.common.exception.ErrorCode.GENERATION_FAIL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastline.aiservice.client.GeminiClient;
import com.fastline.aiservice.domain.RequestLog;
import com.fastline.aiservice.dto.MessageGenerationRequest;
import com.fastline.aiservice.dto.MessageGenerationResponse;
import com.fastline.aiservice.dto.gemini.GeminiRequest;
import com.fastline.aiservice.dto.gemini.GeminiResponse;
import com.fastline.aiservice.repository.AiRepository;
import com.fastline.common.exception.CustomException;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

	private final AiRepository aiRepository;
	private final GeminiClient geminiClient;
	private final ObjectMapper objectMapper;

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.model}")
	private String model;

	@Transactional
	public MessageGenerationResponse generateMessage(MessageGenerationRequest req) {

		RequestLog requestLog = RequestLog.ofRequested(req.orderId());
		aiRepository.save(requestLog);

		String orderInfoString = objectMapper.valueToTree(req).toString();
		String prompt = buildPrompt(orderInfoString);

		try {
			GeminiRequest requestBody = buildBody(prompt);
			GeminiResponse result = geminiClient.generate(requestBody, model, apiKey);

			String raw = result.candidates().get(0).content().parts().get(0).text();

			String sanitized = sanitizeLLM(raw);

			if (!isValidJson(sanitized)) {
				requestLog.fail(prompt, sanitized);
				log.error("[AI JSON validation 실패]\n{}", sanitized);
				throw new CustomException(GENERATION_FAIL);
			}

			MessageGenerationResponse response =
					objectMapper.readValue(sanitized, MessageGenerationResponse.class);

			requestLog.success(prompt, sanitized);
			return response;

		} catch (Exception e) {
			requestLog.fail(prompt, null);
			log.error("[AI 메시지 생성 실패] {}\n{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
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

	private GeminiRequest buildBody(String prompt) {
		return new GeminiRequest(
				List.of(new GeminiRequest.Content(List.of(new GeminiRequest.Part(prompt)))));
	}

	private String sanitizeLLM(String input) {
		if (input == null) return "";
		return input.replace("```json", "").replace("```", "").trim();
	}

	private boolean isValidJson(String input) {
		try {
			objectMapper.readTree(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
