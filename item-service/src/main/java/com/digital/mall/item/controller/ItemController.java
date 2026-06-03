package com.digital.mall.item.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.common.domain.PageQuery;
import com.digital.mall.common.domain.Result;
import com.digital.mall.common.utils.BeanUtils;
import com.digital.mall.item.domain.dto.ItemDTO;
import com.digital.mall.item.domain.dto.OrderDetailDTO;
import com.digital.mall.item.domain.po.Item;
import com.digital.mall.item.service.IItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理相关接口")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    public PageDTO<ItemDTO> queryItemByPage(PageQuery query) {
        Page<Item> result = itemService.page(query.toMpPage("update_time", false));
        return PageDTO.of(result, ItemDTO.class);
    }

    @Operation(summary = "根据id批量查询商品")
    @GetMapping
    public List<ItemDTO> queryItemByIds(@RequestParam("ids") List<Long> ids) {
        return itemService.queryItemByIds(ids);
    }

    @Operation(summary = "根据id查询商品")
    @GetMapping("/{id}")
    public ItemDTO queryItemById(@PathVariable("id") Long id) {
        return BeanUtils.copyBean(itemService.getById(id), ItemDTO.class);
    }

    /*@Operation(summary = "查看商品详情")
    @GetMapping("/detail/{id}")
    public ItemDTO queryItemDetail(@PathVariable("id") Long id) {
        return BeanUtils.copyBean(itemService.getById(id), ItemDTO.class);
    }*/

    @Operation(summary = "新增商品")
    @PostMapping
    public void saveItem(@RequestBody ItemDTO item) {
        itemService.save(BeanUtils.copyBean(item, Item.class));
    }

    @Operation(summary = "更新商品状态")
    @PutMapping("/status/{id}/{status}")
    public void updateItemStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        Item item = new Item();
        item.setId(id);
        item.setStatus(status);
        itemService.updateById(item);
    }

    @Operation(summary = "更新商品")
    @PutMapping
    public void updateItem(@RequestBody ItemDTO item) {
        // 不允许修改商品状态，所以强制设置为null，更新时就会忽略该字段
        item.setStatus(null);
        itemService.updateById(BeanUtils.copyBean(item, Item.class));
    }

    @Operation(summary = "根据id删除商品")
    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable("id") Long id) {
        itemService.removeById(id);
    }

    @Operation(summary = "批量扣减库存")
    @PutMapping("/stock/deduct")
    public void deductStock(@RequestBody List<OrderDetailDTO> items) {
        itemService.deductStock(items);
    }

    @Operation(summary = "归还单个库存")
    @PostMapping("/stock/{id}")
    public void returnStock(@PathVariable("id") Long id) {
        itemService.update()
                .setSql("stock = stock + 1")
                .eq("id", id)
                .gt("stock", 0)
                .update();
    }

    @Operation(summary = "减少单个库存")
    @PostMapping("/reduce/{id}")
    public void reduceStock(@PathVariable("id") Long id) {
        itemService.update()
                .setSql("stock = stock - 1")
                .eq("id", id)
                .gt("stock", 0)
                .update();
    }

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("item-service is running");
    }
}
