package com.nowcoder.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String username;
    private int type;
    private String avatar;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private int status;
    @JsonIgnore
    private String activationCode;
    @JsonIgnore
    private Date createTime;
}
