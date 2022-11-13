package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserContext userContext;

    @Autowired
    private UserService userService;

    @LoginRequired
    @GetMapping("")
    public CommonResult<LoginUser> getUserInfo() {
        LoginUser user = userContext.getUser();
//        if (user == null) {
//            return CommonResult.fail(HttpStatus.BAD_REQUEST, "Please login");
//        }
        return CommonResult.success(user);
    }

    @GetMapping("/{id}")
    public CommonResult<User> getUserById(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        return CommonResult.success(user);
    }

}
