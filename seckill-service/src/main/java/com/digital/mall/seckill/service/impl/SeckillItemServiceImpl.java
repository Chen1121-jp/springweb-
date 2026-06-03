package com.digital.mall.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mall.api.client.ItemClient;
import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.seckill.domain.SeckillItem;
import com.digital.mall.seckill.mapper.SeckillItemMapper;
import com.digital.mall.seckill.service.ISeckillItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillItemServiceImpl extends ServiceImpl<SeckillItemMapper, SeckillItem> implements ISeckillItemService {

    private final ItemClient itemClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveWithItemId(Long itemId) {
        ItemDTO itemDTO = itemClient.queryItemById(itemId);
        if (itemDTO == null) {
            throw new RuntimeException("商品不存在");
        }
        if (itemDTO.getStock() <= 0) {
            throw new RuntimeException("商品已售完");
        }
        SeckillItem seckillItem = new SeckillItem();
        seckillItem.setId(itemId);
        seckillItem.setSeckillPrice(itemDTO.getPrice());
        seckillItem.setStock(itemDTO.getStock());
        seckillItem.setBeginTime(LocalDateTime.now());
        seckillItem.setEndTime(LocalDateTime.now().plusDays(1));
        this.save(seckillItem);
        log.info("添加秒杀商品成功：{}", seckillItem);
        // 将库存缓存到 Redis
        stringRedisTemplate.opsForValue()
                .set("seckill:stock:" + seckillItem.getId(), seckillItem.getStock().toString());
    }

    @Override
    public void returnStock(Long id, int i) {
        SeckillItem seckillItem = this.getById(id);
        seckillItem.setStock(seckillItem.getStock() + i);
        this.updateById(seckillItem);
        log.info("回退库存成功：{}", seckillItem);
        // 回退 item 库存
        itemClient.returnStock(id);
    }
}
