package com.digital.mall.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.mall.pay.domain.dto.PayApplyDTO;
import com.digital.mall.pay.domain.dto.PayOrderFormDTO;
import com.digital.mall.pay.domain.po.PayOrder;

public interface IPayOrderService extends IService<PayOrder> {

    String applyPayOrder(PayApplyDTO applyDTO);

    void tryPayOrderByBalance(PayOrderFormDTO payOrderFormDTO);
}
