package com.nowcoder.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto {
    private Integer postId;
    private String content;
    private Integer commentId;
    private Integer targetId;
}
