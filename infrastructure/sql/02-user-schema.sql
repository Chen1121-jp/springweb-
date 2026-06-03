-- ============================================
-- Schema: "user"
-- 用户服务数据库
-- 注意: user 是 PostgreSQL 保留字，schema 和表名需要双引号
-- ============================================

CREATE SCHEMA IF NOT EXISTS "user";

-- -------------------------------------------
-- Table: "user" (用户)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS "user"."user" (
    id          BIGINT        PRIMARY KEY,
    username    VARCHAR(64)   NOT NULL,
    password    VARCHAR(256)  NOT NULL,
    phone       VARCHAR(32),
    create_time TIMESTAMP,
    update_time TIMESTAMP,
    status      INTEGER       NOT NULL DEFAULT 1,
    balance     INTEGER       NOT NULL DEFAULT 0
);

-- -------------------------------------------
-- Table: address (收货地址)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS "user".address (
    id          BIGINT        PRIMARY KEY,
    user_id     BIGINT        NOT NULL,
    contact     VARCHAR(64),
    mobile      VARCHAR(32),
    province    VARCHAR(64),
    city        VARCHAR(64),
    district    VARCHAR(64),
    detail      VARCHAR(256),
    is_default  BOOLEAN       NOT NULL DEFAULT FALSE
);
