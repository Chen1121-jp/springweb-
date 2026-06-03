package com.digital.mall.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收货地址实体")
public class AddressDTO {
    @Schema(description = "id")
    private Long id;
    @Schema(description = "省")
    private String province;
    @Schema(description = "市")
    private String city;
    @Schema(description = "区")
    private String district;
    @Schema(description = "手机")
    private String mobile;
    @Schema(description = "详细地址")
    private String detail;
    @Schema(description = "联系人")
    private String contact;
    @Schema(description = "是否是默认")
    private Boolean isDefault;
    @Schema(description = "备注")
    private String notes;
}
