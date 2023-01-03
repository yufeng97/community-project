package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.entity.ChatRoom;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserContext userContext;

    @LoginRequired
    @GetMapping("/list")
    public CommonResult<ScrollResult<ChatRoomVo>> getChatList(
            @RequestParam(value = "lastUpdateTime", required = false, defaultValue = "0") long lastUpdateTime) {
        LoginUser loginUser = userContext.getUser();
        if (lastUpdateTime == 0) {
            lastUpdateTime = System.currentTimeMillis();
        }
        Date updateTime = new Date(lastUpdateTime);
        System.out.println("updateTime = " + updateTime);
        Integer userId = loginUser.getUserId();
        List<ChatRoomVo> chatRoomVos = messageService.queryConversationsByUserId(userId, updateTime);
        long totalCount = messageService.queryConversationsCount(userId, updateTime);
        ScrollResult<ChatRoomVo> scrollResult = new ScrollResult<>(chatRoomVos, totalCount);

        return CommonResult.success(scrollResult);
    }

    @LoginRequired
    @GetMapping("/{talkerId}")
    public CommonResult<ScrollResult> getChatMessages(@PathVariable("talkerId") Integer talkerId, @RequestParam long lastUpdateTime) {
        LoginUser loginUser = userContext.getUser();
//        ChatRoom chatRoom = messageService.queryChatRoomById(chatId);
        
//        System.out.println("lastUpdateTime " + lastUpdateTime);
        if (lastUpdateTime == 0) {
            lastUpdateTime = System.currentTimeMillis();
        }
        Integer userId = loginUser.getUserId();
        Date updateTime = new Date(lastUpdateTime);
        List<Message> messages = messageService.queryMessagesByTalkerId(userId, talkerId, updateTime);
        int count = messageService.queryMessageCount(userId, talkerId, updateTime);
        ScrollResult<Message> scrollResult = new ScrollResult<>(messages, count);
        return CommonResult.success(scrollResult);
    }
}
