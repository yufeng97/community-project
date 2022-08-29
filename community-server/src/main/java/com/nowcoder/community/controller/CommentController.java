package com.nowcoder.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.dto.CommentDto;
import com.nowcoder.community.dto.ReplyDto;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Comment2;
import com.nowcoder.community.entity.Pagination;
import com.nowcoder.community.exception.IllegalArgumentException;
import com.nowcoder.community.exception.UnAuthorizationException;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.util.DataPage;
import com.nowcoder.community.util.StringUtils;
import com.nowcoder.community.vo.CommentVo;
import com.nowcoder.community.vo.LoginUser;
import com.nowcoder.community.vo.ReplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserContext userContext;

    @Autowired
    private CommentService commentService;

    @GetMapping("/{postId}/list")
    public CommonResult<DataPage> getPostComments(
            @PathVariable("postId") Integer postId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "7") Integer pageSize,
            @RequestParam(value = "orderBy", required = false, defaultValue = "createTime") String orderBy
    ) {
        int offset = (pageNum - 1) * pageSize;
        int limit = pageSize;
        List<CommentVo> commentVos = commentService.queryPostCommentList(postId, offset, limit, 5);
        long commentCount = commentService.queryPostCommentCount(postId);
        DataPage dataPage = new DataPage();
        dataPage.setRows(commentVos);
        dataPage.setTotal(commentCount);
        return CommonResult.success(dataPage);
    }

    @PostMapping("/{postId}")
    public CommonResult<Boolean> addPostComment(@PathVariable("postId") Integer postId, @RequestBody CommentDto commentDto) {
        LoginUser user = userContext.getUser();
        if (user == null) {
            throw new UnAuthorizationException("请登录");
        }

        if (StringUtils.isBlank(commentDto.getContent())) {
            throw new IllegalArgumentException();
        }

        Comment2 comment = new Comment2();
        comment.setContent(commentDto.getContent());
        comment.setStatus(0);
        comment.setUserId(user.getUserId());
        comment.setPostId(commentDto.getPostId());
        commentService.addComment(comment);
        return null;
    }

    @GetMapping("/{commentId}/reply/list")
    public CommonResult<DataPage> getCommentReplies(
            @PathVariable("commentId") int commentId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum
    ) {
        Pagination pagination = new Pagination();
        pagination.setPageNum(pageNum);
        pagination.setPageSize(5);
        pagination.setOrderBy("create_time asc");
        PageHelper.startPage(pagination);
        List<ReplyVo> replyVos = commentService.queryCommentReplyList(commentId);
        PageInfo<ReplyVo> info = new PageInfo<>(replyVos);
        DataPage dataPage = new DataPage();
        dataPage.setRows(info.getList());
        dataPage.setTotal(info.getTotal());
        return CommonResult.success(dataPage);
    }

    @PostMapping("/{commentId}/reply")
    public CommonResult<Boolean> addCommentReply(@PathVariable("commentId") int commentId, @RequestBody ReplyDto replyDto) {
        LoginUser user = userContext.getUser();
        if (user == null) {
            throw new UnAuthorizationException("请登录");
        }
        if (StringUtils.isBlank(replyDto.getContent())) {
            throw new IllegalArgumentException();
        }
        return null;
    }
}
