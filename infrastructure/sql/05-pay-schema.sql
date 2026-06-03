-- ============================================
-- Schema: pay
-- 支付服务数据库
-- ============================================

CREATE SCHEMA IF NOT EXISTS pay;

-- -------------------------------------------
-- Table: pay_order (支付订单)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS pay.pay_order (
    id               BIGINT        PRIMARY KEY,
    biz_order_no     BIGINT,
    pay_order_no     BIGINT,
    biz_user_id      BIGINT,
    pay_channel_code VARCHAR(32),
    amount           INTEGER       NOT NULL DEFAULT 0,
    pay_type         INTEGER,
    status           INTEGER       NOT NULL DEFAULT 0,
    expand_json      TEXT,
    result_code      VARCHAR(64),
    result_msg       VARCHAR(256),
    pay_success_time TIMESTAMP,
    pay_over_time    TIMESTAMP,
    qr_code_url      VARCHAR(1024),
    create_time      TIMESTAMP,
    update_time      TIMESTAMP,
    creater          BIGINT,
    updater          BIGINT,
    is_delete        BOOLEAN       NOT NULL DEFAULT FALSE
);
