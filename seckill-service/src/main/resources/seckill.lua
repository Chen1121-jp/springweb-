-- 秒杀 Lua 脚本（Redis 原子操作）
-- KEYS: (无，通过 ARGV 传参)
-- ARGV[1] = 秒杀商品 ID (itemId)
-- ARGV[2] = 用户 ID (userId)
-- ARGV[3] = 订单 ID (orderId)
--
-- 返回值:
--   0 = 秒杀成功
--   1 = 库存不足
--   2 = 该用户已参与过此商品的秒杀（一人一单）

local itemId = ARGV[1]
local userId = ARGV[2]
local orderId = ARGV[3]

-- Redis key 定义
local stockKey = "seckill:stock:" .. itemId
local orderKey = "seckill:order:" .. itemId

-- 1. 判断库存是否充足
local stock = tonumber(redis.call('get', stockKey))
if stock == nil or stock <= 0 then
    return 1
end

-- 2. 判断用户是否已经下过单（一人一单）
local isMember = redis.call('sismember', orderKey, userId)
if isMember == 1 then
    return 2
end

-- 3. 扣减库存
redis.call('incrby', stockKey, -1)

-- 4. 记录用户已下单
redis.call('sadd', orderKey, userId)

-- 5. 返回成功
return 0
