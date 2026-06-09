package com.digital.mall.file.controller;

import com.digital.mall.common.domain.Result;
import com.digital.mall.file.domain.dto.FileInfoDTO;
import com.digital.mall.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("file-service is running");
    }

    /**
     * 上传文件
     * @param file   文件
     * @param prefix 存储目录前缀（可选，如 phone、avatar）
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "prefix", required = false) String prefix) {
        try {
            String url = fileService.upload(file, prefix);
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            // 提取 objectName 方便前端记录
            String objectName = url.substring(url.lastIndexOf("/") + 1);
            data.put("objectName", objectName);
            return Result.ok(data);
        } catch (IllegalArgumentException e) {
            log.warn("文件上传参数校验失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param objectName 文件对象名（bucket下的完整路径）
     */
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam("objectName") String objectName) {
        try {
            fileService.delete(objectName);
            return Result.ok();
        } catch (Exception e) {
            log.error("文件删除失败: {}", objectName, e);
            return Result.error("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 列出文件
     * @param prefix 前缀过滤，可选
     */
    @GetMapping("/list")
    public Result<List<FileInfoDTO>> list(@RequestParam(value = "prefix", required = false) String prefix) {
        try {
            List<FileInfoDTO> list = fileService.list(prefix);
            return Result.ok(list);
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            return Result.error("获取文件列表失败: " + e.getMessage());
        }
    }
}
