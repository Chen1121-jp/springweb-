package com.digital.mall.chat.ai.tool;

import java.util.Map;


public interface AiTool {

    String getRiskLevel();

    ToolDefinition getDefinition();

    ToolResult execute(Map<String, Object> params);

}
