package com.digital.mall.file.service.impl;

import com.digital.mall.file.config.MinioProperties;
import com.digital.mall.file.domain.dto.FileInfoDTO;
import com.digital.mall.file.service.FileService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 文件服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /** 允许上传的图片类型 */
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    /** 允许的图片扩展名 */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "webp"
    );

    @Override
    public String upload(MultipartFile file, String prefix) {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("不支持的文件类型：" + contentType + "，仅允许 jpg/png/gif/webp");
        }

        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("文件大小超过限制（最大10MB）");
        }

        // 2. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件扩展名：" + extension);
        }
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + extension.toLowerCase();

        // 3. 构造对象名（支持按业务类别分目录）
        String objectName;
        if (prefix != null && !prefix.isBlank()) {
            // 去掉首尾的 /
            prefix = prefix.trim().replaceAll("^/|/$", "");
            objectName = prefix + "/" + newFileName;
        } else {
            objectName = newFileName;
        }

        // 4. 上传到MinIO
        try {
            PutObjectArgs putArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(putArgs);
            log.info("文件上传成功: {}", objectName);
        } catch (Exception e) {
            log.error("文件上传失败: {}", objectName, e);
            throw new RuntimeException("文件上传失败", e);
        }

        // 5. 返回访问URL
        return minioProperties.getEndpoint() + "/" + minioProperties.getBucket() + "/" + objectName;
    }

    @Override
    public void delete(String objectName) {
        if (objectName == null || objectName.isBlank()) {
            throw new IllegalArgumentException("文件对象名不能为空");
        }
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .build()
            );
            log.info("文件删除成功: {}", objectName);
        } catch (Exception e) {
            log.error("文件删除失败: {}", objectName, e);
            throw new RuntimeException("文件删除失败", e);
        }
    }

    @Override
    public List<FileInfoDTO> list(String prefix) {
        List<FileInfoDTO> result = new ArrayList<>();
        try {
            Iterable<Result<Item>> items = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .prefix(prefix != null ? prefix : "")
                            .recursive(true)
                            .build()
            );
            String bucket = minioProperties.getBucket();
            String endpoint = minioProperties.getEndpoint();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Result<Item> itemResult : items) {
                Item item = itemResult.get();
                ZonedDateTime lastModified = item.lastModified();
                String timeStr = lastModified != null ? lastModified.format(dtf) : "";

                FileInfoDTO dto = FileInfoDTO.builder()
                        .objectName(item.objectName())
                        .fileName(item.objectName().contains("/")
                                ? item.objectName().substring(item.objectName().lastIndexOf("/") + 1)
                                : item.objectName())
                        .size(item.size())
                        .url(endpoint + "/" + bucket + "/" + item.objectName())
                        .lastModified(timeStr)
                        .build();
                result.add(dto);
            }
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            throw new RuntimeException("获取文件列表失败", e);
        }
        return result;
    }

    /**
     * 从原始文件名中提取扩展名
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
