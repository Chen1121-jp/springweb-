package com.digital.mall.item.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.common.domain.PageQuery;
import com.digital.mall.common.domain.Result;
import com.digital.mall.common.utils.BeanUtils;
import com.digital.mall.item.config.MqSyncConfig;
import com.digital.mall.item.domain.dto.ItemDTO;
import com.digital.mall.item.domain.dto.ItemSyncMessage;
import com.digital.mall.item.domain.dto.OrderDetailDTO;
import com.digital.mall.item.domain.po.Item;
import com.digital.mall.item.service.IItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理相关接口")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;
    private final RabbitTemplate rabbitTemplate;

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    public Result<PageDTO<ItemDTO>> queryItemByPage(PageQuery query) {
        Page<Item> result = itemService.page(query.toMpPage("update_time", false));
        return Result.ok(PageDTO.of(result, ItemDTO.class));
    }

    @Operation(summary = "根据id批量查询商品")
    @GetMapping
    public Result<List<ItemDTO>> queryItemByIds(@RequestParam("ids") List<Long> ids) {
        return Result.ok(itemService.queryItemByIds(ids));
    }

    @Operation(summary = "根据id查询商品")
    @GetMapping("/{id}")
    public Result<ItemDTO> queryItemById(@PathVariable("id") Long id) {
        return Result.ok(BeanUtils.copyBean(itemService.getById(id), ItemDTO.class));
    }

    /*@Operation(summary = "查看商品详情")
    @GetMapping("/detail/{id}")
    public ItemDTO queryItemDetail(@PathVariable("id") Long id) {
        return BeanUtils.copyBean(itemService.getById(id), ItemDTO.class);
    }*/

    @Operation(summary = "新增商品")
    @PostMapping
    public Result<Void> saveItem(@RequestBody ItemDTO item) {
        Item entity = BeanUtils.copyBean(item, Item.class);
        itemService.save(entity);
        publishSync("INSERT", entity);
        return Result.ok();
    }

    @Operation(summary = "更新商品状态")
    @PutMapping("/status/{id}/{status}")
    public Result<Void> updateItemStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        Item oldItem = itemService.getById(id);
        Item item = new Item();
        item.setId(id);
        item.setStatus(status);
        itemService.updateById(item);
        if (oldItem != null) {
            Item updated = itemService.getById(id);
            publishSync("UPDATE", updated, oldItem);
        }
        return Result.ok();
    }

    @Operation(summary = "更新商品")
    @PutMapping
    public Result<Void> updateItem(@RequestBody ItemDTO item) {
        Long itemId = item.getId();
        Item oldItem = itemService.getById(itemId);
        item.setStatus(null);
        itemService.updateById(BeanUtils.copyBean(item, Item.class));
        if (oldItem != null) {
            Item updated = itemService.getById(itemId);
            publishSync("UPDATE", updated, oldItem);
        }
        return Result.ok();
    }

    @Operation(summary = "根据id删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> deleteItemById(@PathVariable("id") Long id) {
        Item item = itemService.getById(id);
        itemService.removeById(id);
        if (item != null) {
            publishSync("DELETE", item);
        }
        return Result.ok();
    }

    private void publishSync(String type, Item data, Item oldData) {
        ItemSyncMessage msg = ItemSyncMessage.builder()
            .type(type).data(data).oldData(oldData).build();
        rabbitTemplate.convertAndSend(MqSyncConfig.EXCHANGE,
                                       MqSyncConfig.ROUTING_KEY, msg);
    }

    private void publishSync(String type, Item data) {
        publishSync(type, data, null);
    }

    @Operation(summary = "批量扣减库存")
    @PutMapping("/stock/deduct")
    public Result<Void> deductStock(@RequestBody List<OrderDetailDTO> items) {
        itemService.deductStock(items);
        return Result.ok();
    }

    @Operation(summary = "归还单个库存")
    @PostMapping("/stock/{id}")
    public Result<Void> returnStock(@PathVariable("id") Long id) {
        itemService.update()
                .setSql("stock = stock + 1")
                .eq("id", id)
                .gt("stock", 0)
                .update();
        return Result.ok();
    }

    @Operation(summary = "减少单个库存")
    @PostMapping("/reduce/{id}")
    public Result<Void> reduceStock(@PathVariable("id") Long id) {
        itemService.update()
                .setSql("stock = stock - 1")
                .eq("id", id)
                .gt("stock", 0)
                .update();
        return Result.ok();
    }

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("item-service is running");
    }
}
