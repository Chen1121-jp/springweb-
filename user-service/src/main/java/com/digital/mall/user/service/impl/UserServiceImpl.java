package com.digital.mall.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mall.common.domain.Result;
import com.digital.mall.common.exception.BadRequestException;
import com.digital.mall.common.exception.BizIllegalException;
import com.digital.mall.common.exception.ForbiddenException;
import com.digital.mall.common.utils.UserContext;
import com.digital.mall.user.config.JwtProperties;
import com.digital.mall.user.domain.dto.LoginFormDTO;
import com.digital.mall.user.domain.po.User;
import com.digital.mall.user.domain.vo.UserLoginVO;
import com.digital.mall.user.enums.UserStatus;
import com.digital.mall.user.mapper.UserMapper;
import com.digital.mall.user.service.IUserService;
import com.digital.mall.user.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;

    @Override
    public UserLoginVO login(LoginFormDTO loginDTO) {
        // 1. 数据校验
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        // 2. 根据用户名查询
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if (user == null) {
            throw new BadRequestException("用户名错误");
        }
        // 3. 校验是否禁用
        if (user.getStatus() == UserStatus.FROZEN.getValue()) {
            throw new ForbiddenException("用户被冻结");
        }
        // 4. 校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }
        // 5. 生成 TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        // 6. 封装 VO 返回
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setBalance(user.getBalance());
        vo.setToken(token);
        return vo;
    }

    @Override
    public void deductMoney(String pw, Integer totalFee) {
        log.info("开始扣款");
        // 1. 校验密码
        User user = getById(UserContext.getUser());
        if (user == null || !passwordEncoder.matches(pw, user.getPassword())) {
            throw new BizIllegalException("用户密码错误");
        }


        // 2. 尝试扣款
        try {
            baseMapper.updateMoney(UserContext.getUser(), totalFee);
        } catch (Exception e) {
            throw new RuntimeException("扣款失败，可能是余额不足！", e);
        }
        log.info("扣款成功");
    }

    @Override
    public Result<Void> register(LoginFormDTO loginFormDTO) {
        log.info("开始注册");
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }
        if (lambdaQuery().eq(User::getUsername, username).count() > 0) {
            return Result.error("用户已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.NORMAL.getValue());
        user.setBalance(0);
        save(user);
        return Result.ok();
    }
}
