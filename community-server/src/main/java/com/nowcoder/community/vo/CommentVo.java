package com.nowcoder.community.vo;

import com.nowcoder.community.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentVo {
    int id;
    String content;
    Date createTime;
    int likesCount;
    boolean liked;
    User author;
    int replyCount;
    List<ReplyVo> replies;
}
