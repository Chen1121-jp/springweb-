package com.digital.mall.chat.knowledge;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KnowledgeIndexer {

    private final EmbeddingService embeddingService;
    private final ElasticsearchClient elasticsearchClient;
    private final KnowledgeLoader knowledgeLoader;

    public void buildIndex() {
        try {
            elasticsearchClient.indices().create(c -> c
                    .index("knowledge")
                    .mappings(m -> m
                            .properties("title",p -> p.text(t -> t))
                            .properties("content",p -> p.text(t -> t))
                            .properties("contentVector",p -> p.denseVector(dv -> dv
                                    .dims(768)
                                    .index( true)
                                    .similarity("cosine")
                            ))
                            .properties("source",p -> p.keyword(k -> k))
                    )
            );
        } catch (IOException e) {
            log.error("创建索引失败",e);
        }
    }

    public void indexKnowledge() {
        List<DocumentChunk> documentChunkList = knowledgeLoader.load();
        BulkRequest.Builder bulk = new BulkRequest.Builder();
        for (DocumentChunk documentChunk : documentChunkList) {
            bulk.operations(op -> op.index(idx -> idx
                    .index("knowledge")
                    .id(documentChunk.id())
                    .document(Map.of(
                            "title", documentChunk.title(),
                            "content", documentChunk.content(),
                            "contentVector", embeddingService.embed(documentChunk.content()),
                            "source", documentChunk.source()
                    ))
            ));
        }
        try {
            elasticsearchClient.bulk(bulk.build());
        } catch (IOException e) {
            log.error("索引知识库失败",e);
        }
    }
    @PostConstruct
    public void init() {
        try {
            boolean exists = elasticsearchClient.indices()
                    .exists(e -> e.index("knowledge")).value();
            if (!exists) {
                buildIndex();
                indexKnowledge();
                log.info("知识库索引完成");
            }
        } catch (IOException e) {
            log.error("初始化知识库失败", e);
        }
    }

}
