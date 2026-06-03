package com.digital.mall.cart.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mall.api.client.ItemClient;
import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.cart.domain.dto.CartFormDTO;
import com.digital.mall.cart.domain.po.Cart;
import com.digital.mall.cart.domain.vo.CartVO;
import com.digital.mall.cart.mapper.CartMapper;
import com.digital.mall.cart.service.ICartService;
import com.digital.mall.common.exception.BizIllegalException;
import com.digital.mall.common.utils.BeanUtils;
import com.digital.mall.common.utils.CollUtils;
import com.digital.mall.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    private final ItemClient itemClient;

    @Override
    public void addItem2Cart(CartFormDTO cartFormDTO) {
        // 1. 获取登录用户
        Long userId = UserContext.getUser();

        // 2. 判断是否已经存在
        if (checkItemExists(cartFormDTO.getItemId(), userId)) {
            // 2.1. 存在，则更新数量
            baseMapper.updateNum(cartFormDTO.getItemId(), userId);
            return;
        }
        // 2.2. 不存在，判断是否超过购物车数量
        checkCartsFull(userId);

        // 3. 新增购物车条目

        Cart cart = BeanUtils.copyBean(cartFormDTO, Cart.class);
        cart.setUserId(userId);
        save(cart);
    }

    @Override
    public List<CartVO> queryMyCarts() {
        // 1. 查询我的购物车列表
        List<Cart> carts = lambdaQuery().eq(Cart::getUserId, UserContext.getUser()).list();
        if (CollUtils.isEmpty(carts)) {
            return CollUtils.emptyList();
        }

        // 2. 转换 VO
        List<CartVO> vos = BeanUtils.copyList(carts, CartVO.class);

        // 3. 处理 VO 中的商品信息（远程查询最新价格和状态）
        handleCartItems(vos);

        // 4. 返回
        return vos;
    }

    private void handleCartItems(List<CartVO> vos) {
        // 1. 获取商品 id
        Set<Long> itemIds = vos.stream().map(CartVO::getItemId).collect(Collectors.toSet());
        // 2. 查询商品（Feign 远程调用）
        List<ItemDTO> items = itemClient.queryItemByIds(itemIds);
        if (CollUtils.isEmpty(items)) {
            return;
        }
        // 3. 转为 id → item 的 map
        Map<Long, ItemDTO> itemMap = items.stream()
                .collect(Collectors.toMap(ItemDTO::getId, Function.identity()));
        // 4. 写入 vo
        for (CartVO v : vos) {
            ItemDTO item = itemMap.get(v.getItemId());
            if (item == null) {
                continue;
            }
            v.setNewPrice(item.getPrice());
            v.setStatus(item.getStatus());
            v.setStock(item.getStock());
        }
    }

    @Override
    public void removeByItemIds(Collection<Long> itemIds) {
        // 1. 构建删除条件，userId 和 itemId
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Cart::getUserId, UserContext.getUser())
                .in(Cart::getItemId, itemIds);
        // 2. 删除
        remove(queryWrapper);
    }

    private void checkCartsFull(Long userId) {
        Long count = lambdaQuery().eq(Cart::getUserId, userId).count();
        if (count >= 10) {
            throw new BizIllegalException(StrUtil.format("用户购物车条目不能超过{}", 10));
        }
    }

    private boolean checkItemExists(Long itemId, Long userId) {
        Long count = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getItemId, itemId)
                .count();
        return count > 0;
    }
}
