# Digital Mall 前端设计规格

**日期**: 2026-06-02  
**状态**: 已确认  
**参考**: hmall-nginx (D:\JavaWeb-Project\nginx\hmall-nginx\html\hmall-portal)

---

## 1. 概述

为 Digital Mall 数字商城项目重新设计全套前端页面。设计风格：**暖阳橙 + 奶油白**，简洁大方、明亮温暖。

## 2. 技术方案

- **框架**: Vue 3（CDN 加载，`createApp` + Composition API 风格）
- **HTTP**: axios CDN
- **样式**: 单一 CSS 文件 `digital-mall.css`，CSS Variables 全局主题
- **字体**: Playfair Display（标题）+ Nunito Sans（正文），Google Fonts CDN
- **图标**: Emoji（免 icon 库依赖）
- **部署**: Nginx 静态文件，反向代理 `/api` 到后端网关

## 3. 色彩系统

| 变量 | 色值 | 用途 |
|------|------|------|
| `--primary` | `#FF6B35` | 主按钮、强调、Logo |
| `--primary-dark` | `#E85A26` | Hover 态 |
| `--primary-light` | `#FFF0EB` | 极浅底 |
| `--accent` | `#14B8A6` | 购物车角标、成功 |
| `--bg` | `#FFFBF5` | 页面背景 |
| `--card` | `#FFFFFF` | 卡片 |
| `--text` | `#2D2D2D` | 主文字 |
| `--text-secondary` | `#6B7280` | 次要文字 |
| `--text-light` | `#9CA3AF` | 提示文字 |
| `--border` | `#F0E8E0` | 暖灰边框 |
| `--radius` | `14px` | 圆角 |

## 4. 文件结构

```
infrastructure/nginx/html/digital-mall-portal/
├── index.html              # 首页（Banner + 商品网格 + 秒杀横栏）
├── search.html             # 搜索页（筛选 + 排序 + 商品列表）
├── login.html              # 登录页
├── cart.html               # 购物车
├── order-confirm.html      # 订单确认（地址 + 商品 + 支付方式）
├── pay.html                # 支付页（余额支付）
├── paysuccess.html         # 支付成功
├── seckill.html            # 秒杀页（倒计时 + 进度条）
├── css/
│   └── digital-mall.css    # 全局样式（~300行）
└── js/
    ├── common.js            # axios 配置、拦截器、工具函数
    └── top.js               # 导航栏 Vue 3 全局组件
```

## 5. 页面功能清单

| # | 页面 | API 调用 | 关键功能 |
|---|------|---------|----------|
| 1 | index.html | `GET /items/page` | 导航、Banner轮播、搜索入口、商品网格、秒杀横栏 |
| 2 | search.html | `GET /search/list`, `POST /search/filters` | 关键字搜索、分类/品牌筛选、价格、排序 |
| 3 | login.html | `POST /users/login` | 居中卡片、登录后跳回来源页 |
| 4 | cart.html | `GET /carts`, `PUT /carts`, `DELETE /carts/{id}` | 勾选、数量、删除、总价、去结算 |
| 5 | order-confirm.html | `GET /address`, `POST /orders` | 地址选择、商品明细、支付方式 |
| 6 | pay.html | `POST /pay-orders`, `POST /pay-orders/{id}` | 余额支付、密码、倒计时 |
| 7 | paysuccess.html | `GET /orders/{id}` | 成功动画、订单信息 |
| 8 | seckill.html | `GET /seckill/list`, `POST /seckill/{id}` | 倒计时、库存进度条、一键秒杀 |

## 6. 数据流

```
登录 → sessionStorage{token, user-info}
     → common.js 拦截器自动带 Authorization header
     → 401 → 清除登录态 → 跳转 /login.html

导航栏 top.js → 读取 sessionStorage.user-info → 显示用户名
             → GET /carts → 显示购物车角标数量
```

## 7. 视觉要点

- 按钮全圆角 12px，橙色实底，hover 上浮 2px
- 商品卡片图片顶置，hover 上浮 4px
- 价格大字 #FF6B35，bold 700
- Banner 400px 渐变（珊瑚橙→奶油白），3 条标语轮播
- 秒杀页大号倒计时（等宽字体，红色数字），库存进度条
- 卡片投影带橙色感：`0 2px 12px rgba(255,107,53,.06)`
- 页脚统一 60px 暖灰底

## 8. Nginx 配置

新增 server block 监听端口（如 18083），root 指向 `html/digital-mall-portal`，`/api` 反向代理到后端网关。

## 9. 范围边界

- **包含**: 8 个前端 HTML 页面 + CSS + JS 公共文件 + Nginx 配置
- **不包含**: 后端接口实现、数据库变更、Docker 配置
