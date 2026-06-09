package com.digital.mall.chat.ai.gateway;

import reactor.core.publisher.Flux;

public interface LLMGateway {
    LLMResponse chat(LLMRequest request);
    Flux<String> chatStream(LLMRequest request);
}
