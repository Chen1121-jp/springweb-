package com.digital.mall.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digital.mall.pay.domain.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrder> {
}
