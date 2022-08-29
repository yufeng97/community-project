package com.nowcoder.community.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Comment2 {
    private int id;
    private int userId;
    private int postId;
    private String content;
    private int status;
    private int replyCount;
    private Date createTime;
    private Date updateTime;
}
