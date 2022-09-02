package com.nowcoder.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReplyDto {
    private Integer commentId;
    private String content;
    private Integer toId;
}
