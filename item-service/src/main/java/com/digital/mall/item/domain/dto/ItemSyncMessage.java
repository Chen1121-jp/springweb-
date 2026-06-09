package com.digital.mall.item.domain.dto;

import com.digital.mall.item.domain.po.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSyncMessage {
    /** INSERT / UPDATE / DELETE */
    private String type;
    /** changed data */
    private Item data;
    /** old data (UPDATE only) */
    private Item oldData;
}
