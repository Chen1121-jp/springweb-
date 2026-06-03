package com.digital.mall.pay.enums;

import lombok.Getter;

@Getter
public enum PayType {
    BALANCE(5, "余额支付"),
    H5(1, "h5支付"),
    MINI_APP(2, "小程序支付"),
    MP(3, "公众号支付"),
    QR_CODE(4, "扫码支付");

    private final int value;
    private final String desc;

    PayType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public boolean equalsValue(Integer value) {
        return value != null && this.value == value;
    }
}
