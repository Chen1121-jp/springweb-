package com.digital.mall.chat.knowledge;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KnowledgeSearchService {

    private final EmbeddingService embeddingService;
    private final ElasticsearchClient elasticsearchClient;

    public List<DocumentChunk> search(String query,int topK) {
        float[] embed = embeddingService.embed(query);
        List<Float> queryEmbed = new ArrayList<>();
        for (float f : embed) {
            queryEmbed.add(f);
        }
        try {
            SearchResponse<Map> response = elasticsearchClient.search(s -> s
                    .index("knowledge")
                    .knn(k -> k
                            .field("contentVector")
                            .queryVector(queryEmbed)
                            .k(topK)
                            .numCandidates(topK * 2)
                    )
                    .source(src -> src.filter(f -> f.excludes("contentVector"))), Map.class);

            List<DocumentChunk> documentChunks = new ArrayList<>();
            response.hits().hits().forEach(hit -> {
                Map<String, Object> source = hit.source();
                if (source == null) {return;}
                DocumentChunk documentChunk = new DocumentChunk(hit.id(), (String) source.get("title"),
                        (String) source.get("content"), (String) source.get("source"));
                documentChunks.add(documentChunk);
            });
            return documentChunks;
        }catch (Exception e){
            log.error("搜索失败:query{}",query,e);
            return List.of();
        }

    }

    public List<DocumentChunk> search(String query) {
        return search(query, 5);
    }

}
