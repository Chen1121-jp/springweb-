-- ============================================
-- Schema: item
-- 商品服务数据库
-- ============================================

CREATE SCHEMA IF NOT EXISTS item;

-- -------------------------------------------
-- Table: item (商品)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS item.item (
    id            BIGINT        PRIMARY KEY,
    name          VARCHAR(256)  NOT NULL,
    price         INTEGER       NOT NULL DEFAULT 0,
    stock         INTEGER       NOT NULL DEFAULT 0,
    image         VARCHAR(512),
    category      VARCHAR(128),
    brand         VARCHAR(128),
    spec          TEXT,
    sold          INTEGER       NOT NULL DEFAULT 0,
    comment_count INTEGER       NOT NULL DEFAULT 0,
    is_ad         BOOLEAN       NOT NULL DEFAULT FALSE,
    status        INTEGER       NOT NULL DEFAULT 1,
    create_time   TIMESTAMP,
    update_time   TIMESTAMP,
    creater       BIGINT,
    updater       BIGINT
);
