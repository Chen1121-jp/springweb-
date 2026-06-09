package com.digital.mall.chat.ai.tool;

import com.digital.mall.api.client.SearchClient;
import com.digital.mall.api.client.TradeClient;
import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.api.query.ItemPageQuery;
import com.digital.mall.common.domain.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemSearchTool implements AiTool{

    private final SearchClient searchClient;

    @Override
    public String getRiskLevel() {
        return "low";
    }

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition("search_item","搜索商品，可以按照关键词、品牌、价格区间筛选",
                Map.of("keyword","String","brand","String","minPrice","Integer","maxPrice","Integer"));
    }

    @Override
    public ToolResult execute(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        String brand = (String) params.get("brand");
        Integer minPrice = (Integer) params.get("minPrice");
        Integer maxPrice = (Integer) params.get("maxPrice");
        ItemPageQuery query = new ItemPageQuery();
        query.setKey(keyword);
        query.setBrand(brand);
        query.setMinPrice(minPrice);
        query.setMaxPrice(maxPrice);
        PageDTO<ItemDTO> search = searchClient.search(query);
        if (search == null){
            return new ToolResult(false, null, "搜索不到商品");
        }else {
            return new ToolResult(true, search.getList(), null);
        }
    }
}
