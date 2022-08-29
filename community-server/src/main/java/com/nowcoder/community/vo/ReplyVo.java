package com.nowcoder.community.vo;

import com.nowcoder.community.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class ReplyVo {
    int id;
    String content;
    Date createTime;
    int likesCount;
    boolean liked;
    User author;
    User target;
}
