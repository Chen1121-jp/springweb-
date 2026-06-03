package com.digital.mall.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.mall.seckill.domain.SeckillItem;

public interface ISeckillItemService extends IService<SeckillItem> {

    void saveWithItemId(Long itemId);

    void returnStock(Long id, int i);
}
