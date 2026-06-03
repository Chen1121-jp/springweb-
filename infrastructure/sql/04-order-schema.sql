-- ============================================
-- Schema: "order"
-- 订单服务数据库
-- 注意: order 是 PostgreSQL 保留字，schema 和表名需要双引号
-- ============================================

CREATE SCHEMA IF NOT EXISTS "order";

-- -------------------------------------------
-- Table: "order" (订单)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS "order"."order" (
    id            BIGINT        PRIMARY KEY,
    total_fee     INTEGER       NOT NULL DEFAULT 0,
    payment_type  INTEGER       NOT NULL DEFAULT 0,
    user_id       BIGINT        NOT NULL,
    status        INTEGER       NOT NULL DEFAULT 0,
    create_time   TIMESTAMP,
    pay_time      TIMESTAMP,
    consign_time  TIMESTAMP,
    end_time      TIMESTAMP,
    close_time    TIMESTAMP,
    comment_time  TIMESTAMP,
    update_time   TIMESTAMP
);

-- -------------------------------------------
-- Table: order_detail (订单明细)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS "order".order_detail (
    id          BIGINT        PRIMARY KEY,
    order_id    BIGINT        NOT NULL,
    item_id     BIGINT        NOT NULL,
    num         INTEGER       NOT NULL DEFAULT 1,
    name        VARCHAR(256),
    spec        TEXT,
    price       INTEGER,
    image       VARCHAR(512),
    create_time TIMESTAMP,
    update_time TIMESTAMP
);
