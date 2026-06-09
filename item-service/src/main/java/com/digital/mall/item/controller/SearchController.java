package com.digital.mall.item.controller;

import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.common.domain.Result;
import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.query.ItemPageQuery;
import com.digital.mall.item.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 搜索控制器
 * <p>
 * 提供商品搜索和ES索引同步的REST API。
 * 底层基于 Elasticsearch + IK 中文分词实现全文检索，
 * ES 不可用时自动降级为 MySQL LIKE 模糊查询。
 *
 * @author digital-mall
 */
@Tag(name = "搜索相关接口")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 搜索商品
     * <p>
     * 支持关键词、分类、品牌、价格区间等多条件组合搜索，
     * 支持按价格/销量/更新时间排序，返回分页结果。
     *
     * @param query 搜索条件（关键词、分类、品牌、价格区间、排序、分页）
     * @return 商品分页结果列表
     */
    @Operation(summary = "搜索商品")
    @GetMapping("/list")
    public Result<PageDTO<ItemDocument>> search(ItemPageQuery query) {
        return Result.ok(searchService.search(query));
    }

    /**
     * 全量同步 PostgreSQL 商品数据到 Elasticsearch
     * <p>
     * 先删除旧索引以更新 mapping/settings，再分页读取
     * 所有上架商品（status=1）批量写入ES。用于首次建索引或重建索引。
     *
     * @return 操作结果 {"result": "ok"}
     */
    @Operation(summary = "全量同步PostgreSQL到ES")
    @PostMapping("/admin/sync-all")
    public Result<Map<String, Object>> syncAll() {
        searchService.syncAllFromDatabase();
        return Result.ok(Map.of("result", "ok"));
    }
}
