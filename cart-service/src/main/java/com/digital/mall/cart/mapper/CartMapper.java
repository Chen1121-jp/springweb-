package com.digital.mall.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digital.mall.cart.domain.po.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 更新购物车商品数量（+1）
     */
    void updateNum(@Param("itemId") Long itemId, @Param("userId") Long userId);
}
