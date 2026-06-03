package com.digital.mall.common.domain;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class PageQuery {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String sortBy;
    private Boolean isAsc = true;

    public <T> Page<T> toMpPage(OrderItem... orderItems) {
        Page<T> page = Page.of(pageNo, pageSize);
        if (sortBy != null) {
            OrderItem item = new OrderItem();
            item.setColumn(sortBy);
            item.setAsc(isAsc);
            page.addOrder(item);
        } else if (orderItems != null) {
            page.addOrder(orderItems);
        }
        return page;
    }

    public <T> Page<T> toMpPage(String defaultSortBy, boolean defaultIsAsc) {
        OrderItem item = new OrderItem();
        item.setColumn(defaultSortBy);
        item.setAsc(defaultIsAsc);
        return toMpPage(item);
    }
}
