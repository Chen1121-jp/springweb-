package com.digital.mall.api.client;

import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.api.query.ItemPageQuery;
import com.digital.mall.common.domain.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "item-service", contextId = "search-client")
public interface SearchClient {
    @GetMapping("/search/list")
    PageDTO<ItemDTO> search(@SpringQueryMap ItemPageQuery query);
}
