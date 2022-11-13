package com.nowcoder.community.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nowcoder.community.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PostInfo {
    private Integer id;
    private String title;
    private String brief;
    private Integer type;
    private Date createTime;
    private Integer commentCount;
    private Double score;
    private User author;
    private boolean liked;
    private Long likeCount;
    @JsonIgnore
    private Integer userId;
}
