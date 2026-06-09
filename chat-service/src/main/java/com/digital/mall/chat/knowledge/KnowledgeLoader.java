package com.digital.mall.chat.knowledge;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KnowledgeLoader {


    public List<DocumentChunk> load(){

        List<DocumentChunk> allChunks=new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:knowledge/*.md");
            for (Resource resource : resources) {
                // 获取文件名
                String sourcename = resource.getFilename();
                String content=new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                String title=content.lines()
                        .filter(line -> line.startsWith("# "))
                        .findFirst()
                        .map(line -> line.substring(2))
                        .orElse("未知");
                String[] sections=content.split("\n## ");
                int index=0;
                for (String section : sections) {
                    if (section.trim().isEmpty()){
                        continue;
                    }
                    allChunks.add(new DocumentChunk(sourcename+"-"+String.format("%03d", index++),title, section.trim(),sourcename));
                }
            }
        }catch (IOException e){
            log.error("加载知识库失败",e);
            return List.of();
        }
        return allChunks;

    }
}
