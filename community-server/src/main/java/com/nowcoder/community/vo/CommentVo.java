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
    User target;
    int replyCount;
//    List<ReplyVo> replies;
    List<CommentVo> replies;
}
