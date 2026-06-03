package com.digital.mall.pay.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayOrderVO {
    private Long id;
    private Long bizOrderNo;
    private Long payOrderNo;
    private Long bizUserId;
    private String payChannelCode;
    private Integer amount;
    private Integer payType;
    private Integer status;
    private String expandJson;
    private String resultCode;
    private String resultMsg;
    private LocalDateTime paySuccessTime;
    private LocalDateTime payOverTime;
    private String qrCodeUrl;
    private LocalDateTime createTime;
}
