package com.nowcoder.community.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
public class PostInfo {
    private Integer id;
    private String title;
    private Integer type;
    private Date createTime;
    private Integer commentCount;
    private Double score;
    private Long likeCount;
    private User author;
    @JsonIgnore
    private Integer userId;
}
