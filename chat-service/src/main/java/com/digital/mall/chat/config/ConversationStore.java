package com.digital.mall.chat.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConversationStore {
    private static final String KEY_PREFIX = "chat:conversation:";
    private static final int MAX_HISTORY = 20;
    private static final int TTL_MINUTES = 30;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public List<ChatMessage> getHistory(String userId){
        String history = stringRedisTemplate.opsForValue().get(KEY_PREFIX + userId);
        // 解析历史记录
        if (history == null|| history.isEmpty()){
            return new ArrayList<>();
        }
        try {// 反序列化：JSON 字符串 → List<Map> →List<ChatMessage>
            List<Map<String,String>> historyList = objectMapper.readValue(history, new TypeReference<List<Map<String, String>>>() {});
            List<ChatMessage> messages = new ArrayList<>();
            for (Map<String, String> map : historyList) {
                String role = map.get("role");
                String content = map.get("content");
                if ("user".equals( role)){
                    messages.add(new UserMessage( content));
                }else if ("ai".equals( role)){
                    messages.add(new AiMessage( content));
                }
            }
            return messages;
        }catch (Exception e){
            log.error("解析历史记录失败:userId:{}",userId,e );
        }
        return new ArrayList<>();
    }

    //追加历史记录
    public void saveHistory(String userId,String userMessage,String aiMessage){
        List<ChatMessage> history = getHistory(userId);
        List<Map<String,String>> historyList =messagesToMapList( history);
        historyList.add(Map.of("role", "user", "content", userMessage));
        historyList.add(Map.of("role", "ai", "content", aiMessage));
        while (historyList.size() > MAX_HISTORY){
            historyList.remove(0);
            historyList.remove(0);
        }
        try {
            String json = objectMapper.writeValueAsString(historyList);
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + userId, json, TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("保存历史记录失败:userId:{}",userId,e);
        }


    }

    public void deleteHistory(String userId){
        stringRedisTemplate.delete(KEY_PREFIX + userId);
    }


    /**
     * ChatMessage 列表 → 可序列化的 Map 列表
     */
    private List<Map<String, String>> messagesToMapList(List<ChatMessage> messages) {
        List<Map<String, String>> list = new ArrayList<>();
        for (ChatMessage msg : messages) {
            if (msg instanceof UserMessage) {
                list.add(Map.of("role", "user", "content",
                        ((UserMessage) msg).singleText()));
            } else if (msg instanceof AiMessage) {
                list.add(Map.of("role", "ai", "content",
                        ((AiMessage) msg).text()));
            }
        }
        return list;
    }
}
