package com.digital.mall.chat.controller;

import com.digital.mall.chat.domain.dto.ChatRequest;
import com.digital.mall.chat.ai.gateway.DefaultLLMGateway;
import com.digital.mall.chat.ai.gateway.LLMGateway;
import com.digital.mall.chat.ai.gateway.LLMRequest;
import com.digital.mall.chat.ai.gateway.LLMResponse;
import com.digital.mall.chat.knowledge.DocumentChunk;
import com.digital.mall.chat.knowledge.KnowledgeSearchService;
import com.digital.mall.common.domain.Result;

import com.digital.mall.common.utils.UserContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final LLMGateway gateway;

    private final KnowledgeSearchService knowledgeSearchService;

    public ChatController(LLMGateway gateway, KnowledgeSearchService knowledgeSearchService) {
        this.gateway = gateway;
        this.knowledgeSearchService = knowledgeSearchService;
    }

    @PostMapping("/simple")
    public Result<String> simpleChat(@RequestBody ChatRequest request) {
        String userId = UserContext.getUser() != null ? UserContext.getUser().toString() : "default";
        LLMRequest llmRequest = new LLMRequest(
                UUID.randomUUID().toString(),
                userId,
                userId,
                "simple_chat",
                request.message(),
                Map.of()
        );
        LLMResponse response = gateway.chat(llmRequest);
        return Result.ok(response.content());
    }

    @PostMapping(path = "/stream", produces = "text/plain;charset=UTF-8")
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        String userId = UserContext.getUser() != null ? UserContext.getUser().toString() : "default";
        LLMRequest llmRequest = new LLMRequest(
                UUID.randomUUID().toString(),
                userId,
                userId,
                "stream_chat",
                request.message(),
                Map.of()
        );
        DefaultLLMGateway g = (DefaultLLMGateway) gateway;
        return g.chatStream(llmRequest);
    }

    @PostMapping(path = "/smart-stream", produces = "text/plain;charset=UTF-8")
    public Flux<String> smartChatStream(@RequestBody ChatRequest request) {
        String userId = UserContext.getUser() != null ? UserContext.getUser().toString() : "default";
        LLMRequest llmRequest = new LLMRequest(
                UUID.randomUUID().toString(),
                userId,
                userId,
                "smart_chat",
                request.message(),
                Map.of()
        );
        DefaultLLMGateway g = (DefaultLLMGateway) gateway;
        return g.smartChatStream(llmRequest);
    }


    //测试
    @GetMapping("/knowledge")
    public Result<String> knowledge(@RequestParam String query) {
        String userId = UserContext.getUser() != null ? UserContext.getUser().toString() : "default";
        List<DocumentChunk> search = knowledgeSearchService.search(query);
        return Result.ok(search.toString());
    }
    @GetMapping("/knowledgestream")
    public Result<String> knowledgeStream(@RequestParam String query) {
        String userId = UserContext.getUser() != null ? UserContext.getUser().toString() : "default";
        Flux<String> knowledgeStream = gateway.chatStream(new LLMRequest(
                UUID.randomUUID().toString(),
                userId,
                userId,
                "knowledge_stream",
                query,
                Map.of()
        ));
        knowledgeStream.subscribe(System.out::println);

        return Result.ok("success");

    }
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("chat-service is running");
    }

}