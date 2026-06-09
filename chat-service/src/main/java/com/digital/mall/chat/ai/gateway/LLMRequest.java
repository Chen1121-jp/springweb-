package com.digital.mall.chat.ai.gateway;
import java.util.Map;

public record LLMRequest(
        String requestId,
        String tenantId,
        String userId,
        String scene,
        String userMessage,
        Map<String, Object> variables
) {
}