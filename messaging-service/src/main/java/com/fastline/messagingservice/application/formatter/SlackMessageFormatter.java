package com.fastline.messagingservice.application.formatter;

import com.fastline.messagingservice.application.command.SendMessageCommand;
import com.fastline.messagingservice.presentation.dto.SendMessageRequest;
import com.slack.api.model.Attachment;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SlackMessageFormatter {

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(SendMessageCommand req, String aiResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append("주문 번호 : ").append(req.orderId()).append("\n");
        sb.append("주문자 정보 : ")
                .append(req.customerName())
                .append(" / ")
                .append(req.customerEmail())
                .append("\n");
        sb.append("주문 시간 : ").append(formatDateTime(req.orderDateTime())).append("\n");
        sb.append("상품 정보 : ").append(formatItems(req.items())).append("\n");
        sb.append("요청 사항 : ").append(nullSafe(req.requestNote())).append("\n");
        sb.append("발송지 : ").append(nullSafe(req.sourceHub())).append("\n");
        sb.append("경유지 : ").append(formatViaHubs(req.viaHubs())).append("\n");
        sb.append("도착지 : ").append(nullSafe(req.destination())).append("\n");
        sb.append("배송담당자 : ")
                .append(nullSafe(req.deliveryManagerName()))
                .append(" / ")
                .append(nullSafe(req.deliveryManagerEmail()))
                .append("\n\n");
        sb.append(aiResponse);
        return sb.toString();
    }

    public static Attachment buildAttachment(String text) {
        return Attachment.builder().text(text).build();
    }

    private static String formatItems(List<SendMessageRequest.Item> items) {
        if (items == null || items.isEmpty()) return "정보 없음";
        return items.stream()
                .map(i -> i.name() + " " + i.quantity() + "개")
                .collect(Collectors.joining("\n"));
    }

    private static String formatViaHubs(List<String> viaHubs) {
        if (viaHubs == null || viaHubs.isEmpty()) return "없음";
        return String.join(", ", viaHubs);
    }

    private static String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "정보 없음";
        return dateTime.format(DATETIME_FORMATTER);
    }

    private static String nullSafe(String s) {
        return s == null ? "정보 없음" : s;
    }
}

