package com.digital.mall.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mall.api.client.CartClient;
import com.digital.mall.api.client.ItemClient;
import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.api.dto.OrderDetailDTO;
import com.digital.mall.common.exception.BadRequestException;
import com.digital.mall.common.utils.UserContext;
import com.digital.mall.common.constants.MQConstants;
import com.digital.mall.trade.domain.dto.OrderFormDTO;
import com.digital.mall.trade.domain.po.Order;
import com.digital.mall.trade.domain.po.OrderDetail;
import com.digital.mall.trade.mapper.OrderMapper;
import com.digital.mall.trade.service.IOrderDetailService;
import com.digital.mall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final ItemClient itemClient;
    private final IOrderDetailService detailService;
    private final CartClient cartClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Long createOrder(OrderFormDTO orderFormDTO) {
        // 1. 获取商品 id 和数量的 Map
        List<OrderDetailDTO> detailDTOS = orderFormDTO.getDetails();
        Map<Long, Integer> itemNumMap = detailDTOS.stream()
                .collect(Collectors.toMap(OrderDetailDTO::getItemId, OrderDetailDTO::getNum));
        Set<Long> itemIds = itemNumMap.keySet();

        // 2. 查询商品
        List<ItemDTO> items = itemClient.queryItemByIds(itemIds).getData();
        if (items == null || items.size() < itemIds.size()) {
            throw new BadRequestException("商品不存在");
        }

        // 3. 基于商品价格、购买数量计算商品总价
        int total = 0;
        for (ItemDTO item : items) {
            total += item.getPrice() * itemNumMap.get(item.getId());
        }

        // 4. 组装订单数据
        Order order = new Order();
        order.setTotalFee(total);
        order.setPaymentType(orderFormDTO.getPaymentType());
        order.setUserId(UserContext.getUser());
        order.setStatus(1);
        save(order);

        // 5. 保存订单详情
        List<OrderDetail> details = buildDetails(order.getId(), items, itemNumMap);
        detailService.saveBatch(details);

        // 6. 清理购物车商品
        cartClient.deleteCartItemByIds(itemIds);

        // 7. 扣减库存
        try {
            itemClient.deductStock(detailDTOS);
        } catch (Exception e) {
            throw new RuntimeException("库存不足！");
        }

        // 8. 发送延迟消息（用于超时未支付自动取消）
        rabbitTemplate.convertAndSend(
                MQConstants.DELAY_EXCHANGE_NAME,
                MQConstants.TRADE_DELAY_KEY,
                order.getId(),
                message -> {
                    message.getMessageProperties().setHeader("x-delay", 1000 * 60);
                    return message;
                }
        );

        return order.getId();
    }

    @Override
    public void markOrderPaySuccess(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(2);
        order.setPayTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        // TODO 标记订单为已关闭
        // TODO 恢复库存
    }

    private List<OrderDetail> buildDetails(Long orderId, List<ItemDTO> items, Map<Long, Integer> numMap) {
        List<OrderDetail> details = new ArrayList<>(items.size());
        for (ItemDTO item : items) {
            OrderDetail detail = new OrderDetail();
            detail.setName(item.getName());
            detail.setSpec(item.getSpec());
            detail.setPrice(item.getPrice());
            detail.setNum(numMap.get(item.getId()));
            detail.setItemId(item.getId());
            detail.setImage(item.getImage());
            detail.setOrderId(orderId);
            details.add(detail);
        }
        return details;
    }
}
