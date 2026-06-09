package com.digital.mall.api.client.fallback;

import com.digital.mall.api.client.ItemClient;
import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.api.dto.OrderDetailDTO;
import com.digital.mall.common.domain.Result;
import com.digital.mall.common.exception.BizIllegalException;
import com.digital.mall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;
import java.util.List;

@Slf4j
public class ItemClientFallback implements FallbackFactory<ItemClient> {

    @Override
    public ItemClient create(Throwable cause) {
        return new ItemClient() {
            @Override
            public Result<List<ItemDTO>> queryItemByIds(Collection<Long> ids) {
                log.error("远程调用 ItemClient#queryItemByIds 方法出现异常，参数：{}", ids, cause);
                // 查询购物车允许失败，查询失败返回空集合
                return Result.ok(CollUtils.emptyList());
            }

            @Override
            public void deductStock(List<OrderDetailDTO> items) {
                // 库存扣减业务需要触发事务回滚，查询失败抛出异常
                throw new BizIllegalException(cause != null ? cause.getMessage() : "库存扣减失败");
            }

            @Override
            public Result<ItemDTO> queryItemById(Long id) {
                log.error("远程调用 ItemClient#queryItemById 方法出现异常，id：{}", id, cause);
                return Result.ok(null);
            }

            @Override
            public void returnStock(Long id) {
                log.error("远程调用 ItemClient#returnStock 方法出现异常，id：{}", id, cause);
            }

            @Override
            public void reduceStock(Long id) {
                log.error("远程调用 ItemClient#reduceStock 方法出现异常，id：{}", id, cause);
            }
        };
    }
}
