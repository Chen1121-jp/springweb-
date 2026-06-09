package com.digital.mall.item.service;

import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.dto.ItemSyncMessage;
import com.digital.mall.item.domain.po.Item;
import com.digital.mall.item.repository.ItemDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSyncService {

    private final ItemDocumentRepository documentRepo;

    @RabbitListener(queues = "item.sync.queue")
    public void handleSyncMessage(ItemSyncMessage msg) {
        try {
            switch (msg.getType()) {
                case "INSERT" -> handleInsert(msg.getData());
                case "UPDATE" -> handleUpdate(msg.getData(), msg.getOldData());
                case "DELETE" -> documentRepo.deleteById(msg.getData().getId());
            }
        } catch (Exception e) {
            log.error("Failed to sync item to ES: {}", msg, e);
        }
    }

    private void handleInsert(Item item) {
        if (item != null && item.getStatus() == 1) {
            documentRepo.save(toDocument(item));
            log.debug("ES INSERT: itemId={}", item.getId());
        }
    }

    private void handleUpdate(Item newItem, Item oldItem) {
        boolean wasActive = oldItem != null && oldItem.getStatus() == 1;
        boolean nowActive = newItem != null && newItem.getStatus() == 1;
        if (wasActive && !nowActive) {
            documentRepo.deleteById(newItem.getId());
        } else if (!wasActive && nowActive) {
            documentRepo.save(toDocument(newItem));
        } else if (nowActive) {
            documentRepo.save(toDocument(newItem));
        }
    }

    private ItemDocument toDocument(Item item) {
        return ItemDocument.builder()
            .id(item.getId()).name(item.getName()).brand(item.getBrand())
            .category(item.getCategory()).price(item.getPrice()).stock(item.getStock())
            .image(item.getImage()).spec(item.getSpec()).sold(item.getSold())
            .commentCount(item.getCommentCount()).isAD(item.getIsAD())
            .status(item.getStatus()).createTime(item.getCreateTime())
            .updateTime(item.getUpdateTime()).build();
    }
}
