package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.exception.UnAuthorizationException;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@Slf4j
public class LikeController {

    @Autowired
    private UserContext userContext;

    @Autowired
    private LikeService likeService;

    @LoginRequired
    @PostMapping("/post/{id}")
    public CommonResult<Boolean> postLike(@PathVariable("id") int postId) {
        LoginUser loginUser = userContext.getUser();
        log.debug("postId = " + postId);
        likeService.postLike(loginUser.getUserId(), postId);
        return CommonResult.success(true);
    }

    @LoginRequired
    @PostMapping("/comment/{id}")
    public CommonResult<Boolean> commentLike(@PathVariable("id") int commentId) {
        LoginUser loginUser = userContext.getUser();
        likeService.commentLike(loginUser.getUserId(), commentId);
        return CommonResult.success(true);
    }
}
