package com.digital.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digital.mall.user.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 扣减余额（利用 PostgreSQL 行锁 + balance >= amount 保证不超扣）
     */
    void updateMoney(@Param("userId") Long userId, @Param("amount") Integer amount);
}
