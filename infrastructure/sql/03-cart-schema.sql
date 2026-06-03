-- ============================================
-- Schema: cart
-- 购物车服务数据库
-- ============================================

CREATE SCHEMA IF NOT EXISTS cart;

-- -------------------------------------------
-- Table: cart (购物车)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS cart.cart (
    id          BIGINT        PRIMARY KEY,
    user_id     BIGINT        NOT NULL,
    item_id     BIGINT        NOT NULL,
    num         INTEGER       NOT NULL DEFAULT 1,
    name        VARCHAR(256),
    spec        TEXT,
    price       INTEGER,
    image       VARCHAR(512),
    create_time TIMESTAMP,
    update_time TIMESTAMP
);
