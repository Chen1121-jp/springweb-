package com.digital.mall.chat.ai.tool;

import com.digital.mall.api.client.TradeClient;
import com.digital.mall.api.dto.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderQueryTool implements AiTool{

    private final TradeClient tradeClient;

    @Override
    public String getRiskLevel() {
        return "medium";
    }

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition("query_user_orders", "查询当前用户的所有订单", Map.of());
    }

    @Override
    public ToolResult execute(Map<String, Object> params) {
        List<OrderVO> orderVOS = tradeClient.queryUserOrders();
        if (orderVOS != null){
            return new ToolResult(true, orderVOS, null);
        }else {
            return new ToolResult(false, null, "查询失败");
        }
    }
}
