package com.digital.mall.file.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDTO {
    /** 文件对象名（bucket下路径） */
    private String objectName;
    /** 文件名 */
    private String fileName;
    /** 文件大小（字节） */
    private long size;
    /** 访问URL */
    private String url;
    /** 最后修改时间 */
    private String lastModified;
}
