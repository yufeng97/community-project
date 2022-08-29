package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Reply {
    private Integer id;
    private Integer commentId;
    private Integer fromId;
    private Integer toId;
    private String content;
    private Date createTime;
}
