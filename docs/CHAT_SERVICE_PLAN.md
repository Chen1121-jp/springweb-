# Chat 服务推进计划

基于 [JavaGuide AI 应用架构](https://javaguide.cn/ai/system-design/ai-application-architecture.html) 和 [LLM Gateway 设计](https://javaguide.cn/ai/system-design/llm-gateway.html) 两篇文章的架构理念，结合实际需求制定。

## 需求拆解

| 需求 | 技术实现 | 依赖服务 |
|------|----------|----------|
| 简单对话 | LLM Gateway | DeepSeek API |
| 查看相关政策 | RAG 知识库 | 政策文档 → 向量化 → 检索 |
| 查看订单信息 | Function Calling | trade-service |
| 找到某款具体手机 | Function Calling | item-service |
| 提出购买意见 | RAG + LLM 推理 | item-service |

## 总体架构

```
入口层 → chat.html / nginx / dm-gateway

业务编排 → ChatController（场景判断）

模型网关 → LLMGateway（路由 / fallback / 限流）

  ┌──────────┬──────────────┬──────────────┐
  │   RAG    │   Memory     │    Tool      │
  │ 政策文档  │  会话记忆     │  订单查询     │
  │ 商品信息  │  用户偏好     │  商品搜索     │
  └──────────┴──────────────┴──────────────┘

可观测 → 日志 + Token 统计
```

## 阶段拆解

### 阶段 1：基础对话收尾

| # | 任务 | 说明 | 状态 |
|---|------|------|------|
| 1.1 | 修 SSE `data:` 问题 | `produces` 改 `text/plain;charset=UTF-8` | ⬜ |
| 1.2 | nginx 流式配置 | `/api/chat/` 单独 location，`proxy_buffering off` + `proxy_read_timeout 300s` | ⬜ |
| 1.3 | LLM 请求/响应模型对齐 | 补齐 `requestId`、`scene`、`usage` 等字段 | ⬜ |
| 1.4 | 对话记忆切 Redis | 从 `ConcurrentHashMap` 改为 Redis，TTL 30min | ⬜ |
| 1.5 | 基础调用日志 | requestId、scene、tokens、耗时、错误 | ⬜ |

### 阶段 2：RAG 知识库（政策查询）

| # | 任务 | 说明 | 状态 |
|---|------|------|------|
| 2.1 | 知识库文档准备 | 退换货政策、保修政策等 Markdown | ⬜ |
| 2.2 | 向量存储 | Redis Stack（RediSearch 模块做向量检索） | ⬜ |
| 2.3 | 文档向量化 + 入库 | 分段 → embedding → Redis | ⬜ |
| 2.4 | 检索管线 | `RagService.retrieve(query)` | ⬜ |
| 2.5 | Prompt 模板化 | 区分"普通聊天"和"RAG 问答" | ⬜ |
| 2.6 | 来源引用 | 回答附引用来源 | ⬜ |

### 阶段 3：Function Calling（订单 + 商品查询）

| # | 任务 | 说明 | 状态 |
|---|------|------|------|
| 3.1 | Tool 框架 | `AiTool` 接口：`ToolDefinition` + `ToolResult` + `ToolRiskLevel` | ⬜ |
| 3.2 | 订单查询 Tool | 调 trade-service | ⬜ |
| 3.3 | 商品搜索 Tool | 调 item-service | ⬜ |
| 3.4 | Gateway 扩展 | 支持 Tool Schema，tool_call 执行 | ⬜ |
| 3.5 | 工具权限校验 | userId 校验，敏感操作标记 | ⬜ |
| 3.6 | 前端状态提示 | 调工具时显示"正在查询..." | ⬜ |

### 阶段 4：智能推荐

| # | 任务 | 说明 | 状态 |
|---|------|------|------|
| 4.1 | 需求解析 | LLM 提取：预算/品牌/用途 | ⬜ |
| 4.2 | 多维商品筛选 | 调 item-service 按条件搜 | ⬜ |
| 4.3 | 对比推荐 | LLM 对比分析 + 推荐理由 | ⬜ |
| 4.4 | 用户偏好 Memory | 记录偏好，后续复用 | ⬜ |

### 阶段 5：生产加固

| # | 任务 | 说明 | 状态 |
|---|------|------|------|
| 5.1 | Prompt 版本化 | 从代码抽到配置，支持切换回滚 | ⬜ |
| 5.2 | 模型路由 | `scene` 区分对话/检索/推荐 | ⬜ |
| 5.3 | Token 预算 + 成本统计 | 记录 tokens、耗时、成本 | ⬜ |
| 5.4 | 切回 SSE | 需要多事件时 `text/plain` → `text/event-stream` | ⬜ |
| 5.5 | 评测数据 + 回放 | 收集线上问题 → 固定评测集 → 回归对比 | ⬜ |

## 依赖关系

```
阶段1 → 阶段2 → 阶段3 → 阶段4
                  ↘
                    阶段5（与 4 并行）
```

## 当前：阶段 1 收尾

1. `ChatController`：`produces` → `text/plain`
2. `nginx-1.26.2/conf/nginx.conf`：chat 路由加流式配置
3. 会话记忆切 Redis
4. 加基础日志

---

## 2026-06-08 会话记录

### 阶段进度

| 阶段 | 状态 |
|------|------|
| 阶段 1：基础对话 | ✅ 完成 |
| 阶段 2：RAG 知识库 | ✅ 完成（2.1~2.6） |
| 阶段 3：Function Calling | 🔧 进行中（3.1~3.5 ✅，3.6 ✅，3.7 工具执行循环待修） |
| 阶段 4：智能推荐 | ⬜ 未开始 |
| 阶段 5：生产加固 | ⬜ 未开始 |

### 新增文件

| 文件 | 说明 |
|------|------|
| `chat-service/.../knowledge/KnowledgeSearchService.java` | ES knn 向量检索服务 |
| `chat-service/.../tool/AiTool.java` | 工具接口（getDefinition + execute + getRiskLevel） |
| `chat-service/.../tool/ToolDefinition.java` | 工具定义 record |
| `chat-service/.../tool/ToolResult.java` | 工具执行结果 record |
| `chat-service/.../tool/OrderQueryTool.java` | 订单查询工具（调 trade-service） |
| `chat-service/.../tool/ItemSearchTool.java` | 商品搜索工具（调 item-service） |
| `chat-service/.../tool/ChatTool.java` | LangChain4j 官方 @Tool 注解工具类 |
| `chat-service/.../knowledge/return-policy.md` | 退换货政策 |
| `chat-service/.../knowledge/warranty-policy.md` | 保修政策 |
| `chat-service/.../knowledge/shipping-policy.md` | 配送政策 |
| `chat-service/.../knowledge/privacy-policy.md` | 隐私政策 |

### 修改文件

| 文件 | 改动 |
|------|------|
| `DefaultLLMGateway.java` | 新增 smartChatStream、buildDecisionPrompt、buildToolsPrompt、findTool、handleToolCall、buildSmartSystemMessage；注入 KnowledgeSearchService + ChatTool + List<AiTool> |
| `ChatController.java` | 新增 /smart-stream、/knowledge 接口 |
| `ChatApplication.java` | 加 @EnableFeignClients |
| `chat.html` | 请求路径改为 /smart-stream，加 toolStatus 状态提示 |
| `application.yml` | 加 Feign 直连 URL（临时绕过 Nacos），加 DEBUG 日志 |

### 当前问题

1. **AiServices + DeepSeek 流式不兼容**：smartChatStream 用了 LangChain4j 官方的 AiServices + @Tool 方案，但 DeepSeek 流式接口可能不支持 function calling，工具不会被调用。需改为 Decision 阶段用非流式模型、结果拿到后再流式输出。
2. **Nacos 服务发现失败**：Feign 调 item-service 报 UnknownHostException，已用直连 URL 临时绕过，根源未解决。
3. **新旧两套工具代码并存**：ChatTool（官方 @Tool）和 AiTool 那套（手动）都在，以后可删一套。
