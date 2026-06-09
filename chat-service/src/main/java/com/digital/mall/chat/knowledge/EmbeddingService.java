package com.digital.mall.chat.knowledge;

import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    /**
     * 把文本转成向量
     */
    public float[] embed(String text) {
        return embeddingModel.embed(text).content().vector();
    }
}
