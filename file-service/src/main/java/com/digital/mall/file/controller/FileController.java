package com.digital.mall.file.controller;

import com.digital.mall.common.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {

    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("file-service is running");
    }
}
