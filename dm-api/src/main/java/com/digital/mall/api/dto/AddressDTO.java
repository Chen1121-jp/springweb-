package com.digital.mall.api.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private Long userId;
    private String contact;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
    private Boolean isDefault;
}
