package com.digital.mall.chat.ai.tool;

import com.digital.mall.api.client.SearchClient;
import com.digital.mall.api.client.TradeClient;
import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.api.dto.OrderVO;
import com.digital.mall.api.query.ItemPageQuery;
import com.digital.mall.common.domain.PageDTO;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatTool {

    private final TradeClient tradeClient;
    private final SearchClient searchClient;

    @Tool("查询当前用户的所有订单")
    public List<OrderVO> queryUserOrders() {
        return tradeClient.queryUserOrders();
    }

    @Tool("搜索商城商品")
    public List<ItemDTO> searchItems(@P("搜索关键词") String
                                             keyword) {
        ItemPageQuery query = new ItemPageQuery();
        query.setKey(keyword);
        PageDTO<ItemDTO> result = searchClient.search(query);
        return result.getList();
    }
}