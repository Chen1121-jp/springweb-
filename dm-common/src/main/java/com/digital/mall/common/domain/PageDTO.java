package com.digital.mall.common.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.mall.common.utils.BeanUtils;
import lombok.Data;

import java.util.List;

@Data
public class PageDTO<T> {
    private Long total;
    private Long pages;
    private List<T> list;

    public static <T, PO> PageDTO<T> of(Page<PO> page, Class<T> clazz) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setTotal(page.getTotal());
        dto.setPages(page.getPages());
        dto.setList(BeanUtils.copyList(page.getRecords(), clazz));
        return dto;
    }
}
