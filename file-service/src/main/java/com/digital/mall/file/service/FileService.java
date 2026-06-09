package com.digital.mall.file.service;

import com.digital.mall.file.domain.dto.FileInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务接口
 */
public interface FileService {

    /**
     * 上传文件到MinIO
     * @param file   文件
     * @param prefix 存储路径前缀（如 phone、avatar）
     * @return 文件访问URL
     */
    String upload(MultipartFile file, String prefix);

    /**
     * 删除MinIO中的文件
     * @param objectName 文件对象名（bucket下的完整路径）
     */
    void delete(String objectName);

    /**
     * 列出桶中文件
     * @param prefix 前缀过滤，null则列出所有
     * @return 文件信息列表
     */
    List<FileInfoDTO> list(String prefix);
}
