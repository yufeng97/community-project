package com.nowcoder.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.dto.CommentDto;
import com.nowcoder.community.dto.ReplyDto;
import com.nowcoder.community.entity.Comment2;
import com.nowcoder.community.entity.Pagination;
import com.nowcoder.community.entity.Reply;
import com.nowcoder.community.exception.CommentNotExistException;
import com.nowcoder.community.exception.IllegalArgumentException;
import com.nowcoder.community.exception.PostNotExistException;
import com.nowcoder.community.exception.UnAuthorizationException;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.util.DataPage;
import com.nowcoder.community.util.StringUtils;
import com.nowcoder.community.vo.CommentVo;
import com.nowcoder.community.vo.LoginUser;
import com.nowcoder.community.vo.ReplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserContext userContext;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

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

    @PostMapping("")
    public CommonResult<Boolean> addPostComment(@RequestBody CommentDto commentDto, HttpServletResponse response) {
        LoginUser user = userContext.getUser();
        if (user == null) {
            throw new UnAuthorizationException("请登录");
        }

        String content = commentDto.getContent();
        Integer postId = commentDto.getPostId();
        if (StringUtils.isBlank(content) || postId == null) {
            throw new IllegalArgumentException();
        }

        if (!discussPostService.checkPostExistById(postId)) {
            throw new PostNotExistException();
        }

        Comment2 comment = new Comment2();
        comment.setContent(content);
        comment.setUserId(user.getUserId());
        comment.setPostId(postId);
        commentService.addComment(comment);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return CommonResult.success();
    }

    @PostMapping("/reply")
    public CommonResult<Boolean> addCommentReply(@RequestBody ReplyDto replyDto, HttpServletResponse response) {
        LoginUser user = userContext.getUser();
        if (user == null) {
            throw new UnAuthorizationException("请登录");
        }

        Integer commentId = replyDto.getCommentId();
        String content = replyDto.getContent();
        if (commentId == null || StringUtils.isBlank(content)) {
            throw new IllegalArgumentException();
        }
        if (!commentService.checkCommentExistById(commentId)) {
            throw new CommentNotExistException();
        }

        Reply reply = new Reply();
        reply.setFromId(user.getUserId());
        reply.setContent(replyDto.getContent());
        reply.setCommentId(replyDto.getCommentId());
        reply.setToId(replyDto.getToId());
        commentService.addReply(reply);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return CommonResult.success();
    }
}
