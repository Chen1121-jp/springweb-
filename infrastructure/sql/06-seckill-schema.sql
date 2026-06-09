-- ============================================
-- Schema: seckill
-- 秒杀服务数据库
-- ============================================

CREATE SCHEMA IF NOT EXISTS seckill;

-- -------------------------------------------
-- Table: seckill_item (秒杀商品)
-- IdType: INPUT (手动赋值 = item.id)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS seckill.seckill_item (
    id              BIGINT        PRIMARY KEY,
    name            VARCHAR(255),
    image           VARCHAR(512),
    original_price  INTEGER,
    seckill_price   INTEGER       NOT NULL,
    stock           INTEGER       NOT NULL,
    begin_time      TIMESTAMP     NOT NULL,
    end_time        TIMESTAMP     NOT NULL,
    max_purchase    INTEGER       NOT NULL DEFAULT 1,
    create_time     TIMESTAMP,
    update_time     TIMESTAMP
);

-- -------------------------------------------
-- Table: seckill_order (秒杀订单)
-- IdType: ASSIGN_ID (Snowflake, 应用端生成)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS seckill.seckill_order (
    id          BIGINT        PRIMARY KEY,
    user_id     BIGINT        NOT NULL,
    item_id     BIGINT        NOT NULL,
    status      INTEGER       NOT NULL DEFAULT 0,
    create_time TIMESTAMP,
    pay_time    TIMESTAMP,
    update_time TIMESTAMP
);
