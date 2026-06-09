package com.digital.mall.api.client;

import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.api.query.ItemPageQuery;
import com.digital.mall.common.domain.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 搜索服务 Feign 客户端
 * <p>
 * 供其他微服务远程调用 item-service 的商品搜索接口。
 * 通过 Spring Cloud OpenFeign + Nacos 实现服务发现与负载均衡。
 *
 * @author digital-mall
 */
@FeignClient(value = "item-service", contextId = "search-client")
public interface SearchClient {

    /**
     * 远程搜索商品
     * <p>
     * 调用 item-service 的 /search/list 接口进行多条件组合搜索。
     *
     * @param query 搜索条件（关键词、分类、品牌、价格区间、排序、分页）
     * @return 商品分页结果
     */
    @GetMapping("/search/list")
    PageDTO<ItemDTO> search(@SpringQueryMap ItemPageQuery query);
}
