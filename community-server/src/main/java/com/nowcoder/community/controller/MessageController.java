package com.nowcoder.community.controller;

import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

//    @GetMapping("")
//    public CommonResult getChatList(
//            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
//            @RequestParam(value = "pageSize", required = false, defaultValue = "7") Integer pageSize,
//            @RequestParam(value = "orderBy", required = false, defaultValue = "createTime") String orderBy) {
//
//    }
}
