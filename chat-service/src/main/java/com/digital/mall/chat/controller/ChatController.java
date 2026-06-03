package com.digital.mall.chat.controller;

import com.digital.mall.common.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("chat-service is running");
    }
}
