package com.digital.mall.user.controller;

import com.digital.mall.common.domain.Result;
import com.digital.mall.user.domain.dto.LoginFormDTO;
import com.digital.mall.user.domain.vo.UserLoginVO;
import com.digital.mall.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Validated LoginFormDTO loginFormDTO) {
        return userService.register(loginFormDTO);
    }
    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public UserLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO) {
        return userService.login(loginFormDTO);
    }

    @Operation(summary = "扣减余额")
    @PutMapping("/money/deduct")
    public void deductMoney(
            @Parameter(description = "支付密码") @RequestParam("pw") String pw,
            @Parameter(description = "支付金额") @RequestParam("amount") Integer amount) {
        userService.deductMoney(pw, amount);
    }

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("user-service is running");
    }
}
