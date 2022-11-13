package com.nowcoder.community.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class PostVo extends PostInfo {
    private String content;
}
