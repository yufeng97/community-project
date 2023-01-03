package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserContext userContext;

    @Autowired
    private UserService userService;

    @LoginRequired
    @GetMapping("/me")
    public CommonResult<LoginUser> getUserInfo() {
        LoginUser user = userContext.getUser();
        return CommonResult.success(user);
    }

    @GetMapping("/{id}")
    public CommonResult<User> getUserById(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        return CommonResult.success(user);
    }

    @GetMapping("")
    public CommonResult<Map<Integer, User>> getUserByListId(@RequestParam(value = "ids") List<Integer> ids) {
        Map<Integer, User> userMap = userService.queryUserByIds(ids);
        return CommonResult.success(userMap);
    }

}
