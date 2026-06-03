package com.digital.mall.user.controller;

import com.digital.mall.common.exception.BadRequestException;
import com.digital.mall.common.utils.BeanUtils;
import com.digital.mall.common.utils.CollUtils;
import com.digital.mall.common.utils.UserContext;
import com.digital.mall.user.domain.dto.AddressDTO;
import com.digital.mall.user.domain.po.Address;
import com.digital.mall.user.service.IAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收货地址管理接口")
@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @Operation(summary = "根据id查询地址")
    @GetMapping("/{addressId}")
    public AddressDTO findAddressById(@Parameter(description = "地址id") @PathVariable("addressId") Long id) {
        Address address = addressService.getById(id);
        // 判断当前用户
        Long userId = UserContext.getUser();
        if (!address.getUserId().equals(userId)) {
            throw new BadRequestException("地址不属于当前登录用户");
        }
        return BeanUtils.copyBean(address, AddressDTO.class);
    }

    @Operation(summary = "查询当前用户地址列表")
    @GetMapping
    public List<AddressDTO> findMyAddresses() {
        List<Address> list = addressService.query()
                .eq("user_id", UserContext.getUser())
                .list();
        if (CollUtils.isEmpty(list)) {
            return CollUtils.emptyList();
        }
        return BeanUtils.copyList(list, AddressDTO.class);
    }

    @Operation(summary = "新增地址")
    @PostMapping
    public Long addAddress(@RequestBody AddressDTO addressDTO) {
        Address address = BeanUtils.copyBean(addressDTO, Address.class);
        address.setUserId(UserContext.getUser());
        addressService.save(address);
        return address.getId();
    }
}
