package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ChatRoom {
    private Integer id;
    private Integer user1Id;
    private Integer user2Id;
    private String lastMessage;
    private Date updateTime;
}
