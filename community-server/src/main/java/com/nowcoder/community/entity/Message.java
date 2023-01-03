package com.nowcoder.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private int id;
    private int senderId;
    private int recipientId;
    private String content;
    private int status;
    private Date createTime;
    private Date readTime;
    @JsonIgnore
    private int senderDeleted;
    @JsonIgnore
    private int recipientDeleted;
}
