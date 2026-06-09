package com.digital.mall.chat.ai.gateway;

public record LLMResponse(
        String requestId,
        String model,
        String content,
        int inputTokens,
        int outputTokens,
        String finishReason
) {
}
