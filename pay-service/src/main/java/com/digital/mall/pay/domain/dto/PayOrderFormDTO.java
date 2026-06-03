package com.digital.mall.pay.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "支付确认表单实体")
public class PayOrderFormDTO {
    @Schema(description = "支付订单id不能为空")
    @NotNull(message = "支付订单id不能为空")
    private Long id;

    @Schema(description = "支付密码")
    @NotNull(message = "支付密码")
    private String pw;
}
