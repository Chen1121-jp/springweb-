package com.digital.mall.chat.ai.gateway;

import com.digital.mall.chat.ai.tool.AiTool;
import com.digital.mall.chat.ai.tool.ChatTool;
import com.digital.mall.chat.ai.tool.ToolDefinition;
import com.digital.mall.chat.config.ConversationStore;
import com.digital.mall.chat.knowledge.DocumentChunk;
import com.digital.mall.chat.knowledge.KnowledgeSearchService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import com.digital.mall.chat.ai.tool.ToolResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultLLMGateway implements LLMGateway {

    private final ChatLanguageModel chatModel;
    private final StreamingChatLanguageModel streamingChatModel;

    private final ConversationStore conversationStore;
    private final KnowledgeSearchService knowledgeSearchService;

    private final ChatTool chatTool;
    private final List<AiTool> tools;

    interface Assistant {
        dev.langchain4j.service.TokenStream
        chat(@dev.langchain4j.service.UserMessage String userMessage);
    }


    @Override
    public LLMResponse chat(LLMRequest request) {
        String conversationId = request.userId();
        List<ChatMessage> history = conversationStore.getHistory(conversationId);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new SystemMessage("""
                你是数码商城的AI客服助手，名字叫"小电"。你的职责是：
                - 回答用户关于商品的问题
                - 用中文回复，语气亲切友好，简洁明了
                - 不要编造数据，不知道就说不知道
                - 如果用户问超出商城范围的问题，礼貌地引导回商城话题
                """));
        messages.addAll(history);
        messages.add(new UserMessage(request.userMessage()));

        String answer = chatModel.chat(messages.toArray(new ChatMessage[0]))
                .aiMessage().text();

        conversationStore.saveHistory(conversationId, request.userMessage(), answer);


        return new LLMResponse(request.requestId(), "unknown", answer, 0, 0, "stop");
    }

    public Flux<String> chatStream(LLMRequest request) {
        String conversationId = request.userId();
        List<ChatMessage> history = conversationStore.getHistory(conversationId);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(buildSystemMessage(request.userMessage()));
        messages.addAll(history);
        messages.add(new UserMessage(request.userMessage()));

        StringBuilder fullAnswer = new StringBuilder();
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        streamingChatModel.chat(messages,
                new StreamingChatResponseHandler() {
                    @Override
                    public void onPartialResponse(String partialResponse) {
                        fullAnswer.append(partialResponse);
                        sink.tryEmitNext(partialResponse);
                    }

                    @Override
                    public void onCompleteResponse(ChatResponse completeResponse) {
                        conversationStore.saveHistory(conversationId, request.userMessage(), fullAnswer.toString());

                        log.info("chat stream complete: userId={}, inputTokens={},outputTokens={}",
                                request.userId(),
                                completeResponse.tokenUsage().inputTokenCount(),
                                completeResponse.tokenUsage().outputTokenCount());
                        sink.tryEmitComplete();
                    }

                    @Override
                    public void onError(Throwable error) {
                        sink.tryEmitError(error);
                        log.error("chat stream error: userId={}", request.userId(),
                                error);
                    }
                });

        return sink.asFlux();
    }

    public Flux<String> smartChatStream(LLMRequest request){
        String conversationId = request.userId();
        String userQuery = request.userMessage();

        // RAG + System Prompt
        String systemMessage = buildSmartSystemMessage(userQuery);

        // AiServices 自动处理工具调用
        Assistant assistant = AiServices.builder(Assistant.class)
                .streamingChatLanguageModel(streamingChatModel)
                .systemMessageProvider(memoryId -> systemMessage)
                .tools(chatTool)
                .build();

        TokenStream tokenStream = assistant.chat(userQuery);

        StringBuilder fullAnswer = new StringBuilder();
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        tokenStream.onPartialResponse(partial -> {
            fullAnswer.append(partial);
            sink.tryEmitNext(partial);
        }).onCompleteResponse(response -> {
            conversationStore.saveHistory(conversationId, userQuery, fullAnswer.toString());
            sink.tryEmitComplete();
        }).onError(error -> {
            log.error("smart chat error", error);
            sink.tryEmitError(error);
        }).start();

        return sink.asFlux();
    }
    private String buildSmartSystemMessage(String userQuery) {
        List<DocumentChunk> chunks =
                knowledgeSearchService.search(userQuery, 3);

        StringBuilder prompt = new StringBuilder();
        prompt.append("你是数码商城AI客服小电。\n\n");

        if (!chunks.isEmpty()) {
            prompt.append("【知识库文档】\n");
            for (DocumentChunk c : chunks) {

                prompt.append("---\n").append(c.content()).append("\n");
            }
        }

        prompt.append("\n如果需要查询订单或搜索商品，请调用对应工具获取数据。");
        return prompt.toString();
    }

    private SystemMessage buildSystemMessage(String userQuery) {
        List<DocumentChunk> documentChunks = knowledgeSearchService.search(userQuery,3);
        StringBuilder knowledge = new StringBuilder();
        for (DocumentChunk documentChunk : documentChunks) {
            knowledge.append("[").append(documentChunk.title()).append("]\n");
            knowledge.append("来源：").append(documentChunk.source()).append("\n");
            knowledge.append(documentChunk.content()).append("\n\n");
        }
        if (documentChunks!=null && documentChunks.size()>0){
            return SystemMessage.systemMessage("""
                    请根据以下知识库文档回答用户问题
                    如果文档无法回答问题，请如实告知
                    回答末尾请注明参考来源。
                    """+knowledge.toString()+ """
                            你可以使用一下工具获取信息：
                            """+buildToolsPrompt()+ """
                            如果需要使用工具，请回复：
                            {"tool":"工具名","params":{...}}"""
                    );
        }else {
            return SystemMessage.systemMessage("""
                你是数码商城的AI客服助手，名字叫"小电"。你的职责是：
                - 回答用户关于商品问题
                - 用中文回复，语气亲切友好，简洁明了
                - 不要编造数据，不知道就说不知道
                - 如果用户问超出商城范围问题，礼貌地引导回商城话题
                - 你可以使用一下工具获取信息：
                  """+buildToolsPrompt()+ """
                  如果需要使用工具，请回复：
                  {"tool":"工具名","params":{...}}"""
            );
        }
    }

    private String buildToolsPrompt(){
        StringBuilder toolsPrompt = new StringBuilder();
        for (AiTool tool : tools) {
            ToolDefinition definition = tool.getDefinition();
            toolsPrompt.append("-").append(definition.name()).append(": ")
                    .append(definition.description()).append("\n");
        }
        return toolsPrompt.toString();
    }

    private SystemMessage buildDecisionPrompt(String userQuery) {
        List<DocumentChunk> chunks = knowledgeSearchService.search(userQuery, 3);
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是数码商城AI客服。请判断用户问题是否需要查询数据：\n\n");
        if (!chunks.isEmpty()) {
            prompt.append("【知识库文档】\n");
            for (DocumentChunk c : chunks) {
                prompt.append("---\n").append(c.content()).append("\n");
            }
        }
        prompt.append("\n【可用工具】\n");
        prompt.append(buildToolsPrompt());
        prompt.append("\n规则：\n");
        prompt.append("- 需要查订单/搜商品 → 返回 {\"tool\":\"工具名\",\"params\":{}}\n");
        prompt.append("- 能直接回答 → 直接回复用户\n");
        return SystemMessage.systemMessage(prompt.toString());
    }

    private AiTool findTool(String name) {
        for (AiTool tool : tools) {
            if (tool.getDefinition().name().equals(name)) {
                return tool;
            }
        }
        return null;
    }

    private String handleToolCall(String toolCall) {
        if (toolCall == null) return null;
        int start = toolCall.indexOf("{\"tool\"");
        if (start < 0) return null;
        int braceCount = 0;
        int end = -1;
        for (int i = start; i < toolCall.length(); i++) {
            char c = toolCall.charAt(i);
            if (c == '{') braceCount++;
            else if (c == '}') {
                braceCount--;
                if (braceCount == 0) { end = i; break; }
            }
        }
        if (end < 0) return null;
        String jsonStr = toolCall.substring(start, end + 1);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> json = objectMapper.readValue(jsonStr, Map.class);
            String toolName = (String) json.get("tool");
            if (toolName == null) return null;
            AiTool tool = findTool(toolName);
            if (tool == null) return null;
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>) json.getOrDefault("params", Map.of());
            ToolResult result = tool.execute(params);
            if (result.success()) {
                return result.data().toString();
            } else {
                return "工具执行失败：" + result.error();
            }
        } catch (Exception e) {
            log.error("工具调用解析失败", e);
            return "工具调用解析失败：" + e.getMessage();
        }
    }

}