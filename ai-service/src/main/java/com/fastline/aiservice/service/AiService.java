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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

		RequestLog log = RequestLog.ofRequested(req.orderId());
		aiRepository.save(log);

		String orderInfoString = objectMapper.valueToTree(req).toString();
		String prompt = buildPrompt(orderInfoString);

		try {

			GeminiRequest requestBody = buildBody(prompt);
			GeminiResponse result = geminiClient.generate(requestBody, model, apiKey);

			String raw = result.candidates().get(0).content().parts().get(0).text();
			MessageGenerationResponse response =
					objectMapper.readValue(raw, MessageGenerationResponse.class);

			log.success(prompt, raw);
			return response;

		} catch (Exception e) {
			log.fail(prompt, null);
			throw new CustomException(GENERATION_FAIL);
		}
	}

	private String buildPrompt(String orderInfoString) {
		return """
						아래 주문 정보를 고려하여, 해당 주문이 납기 요청을 맞추기 위해
						최종 발송되어야 하는 데드라인 시각을 산출해줘.

						### 요구사항
						- 요청자가 요구한 도착 납기시점 + 배송 담당자 근무시간(09 ~ 18) 을 고려
						- 경유지(센터) 이동 시간을 적절히 합리적으로 추정해서 반영
						- 결과 응답은 JSONObject 아래와 같이 1개의 항목을 포함:
						{
								"finalDispatchDeadline": "위 내용을 기반으로 도출된 최종 발송 시한은 MM월 DD일 오전/오후 HH시 입니다."
						}
						- 절대로 ```json 같은 코드 fencing 없이 pure JSON 문자열만 출력

						### 주문 정보
						%s

						정확한 JSON으로만 답변해줘.
						"""
				.formatted(orderInfoString);
	}

	private GeminiRequest buildBody(String prompt) {
		return new GeminiRequest(
				List.of(new GeminiRequest.Content(List.of(new GeminiRequest.Part(prompt)))));
	}
}
