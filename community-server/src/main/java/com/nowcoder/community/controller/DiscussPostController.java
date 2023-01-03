package com.nowcoder.community.controller;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.dto.PostDto;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Pagination;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.DataPage;
import com.nowcoder.community.vo.LoginUser;
import com.nowcoder.community.vo.PostInfo;
import com.nowcoder.community.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/post")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private UserContext userContext;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @LoginRequired
    @PostMapping("")
    public CommonResult<Boolean> addDiscussPost(@RequestBody PostDto postDto, HttpServletResponse response) {
        LoginUser user = userContext.getUser();

        DiscussPost post = new DiscussPost();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUserId(user.getUserId());
        discussPostService.addDiscussPost(post);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return CommonResult.success();
    }

    @GetMapping("/list")
    public CommonResult<DataPage<PostInfo>> getDiscussPostList(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "7") Integer pageSize,
            @RequestParam(value = "orderBy", required = false, defaultValue = "createTime") String orderBy) {
        LoginUser user = userContext.getUser();
        Pagination pagination = new Pagination();
        pagination.setPageNum(pageNum);
        pagination.setPageSize(pageSize);
        switch (orderBy) {
            case "commentCount":
                pagination.setOrderBy("discuss_post.comment_count desc");
                break;
            case "createTime":
            default:
                pagination.setOrderBy("discuss_post.create_time desc");
                break;
        }

        List<PostInfo> postInfos = discussPostService.queryPostInfoList(pagination);
        PageInfo<PostInfo> info = new PageInfo<>(postInfos);
        DataPage<PostInfo> dataPage = new DataPage<>();
        dataPage.setRows(info.getList());
        dataPage.setTotal(info.getTotal());
        return CommonResult.success(dataPage);
    }

    @GetMapping("/{id}")
    public CommonResult<PostVo> getDiscussPostById(@PathVariable("id") Integer postId) {
        PostVo postVo = discussPostService.queryPostById(postId);

//        // 点赞数量
//        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
//        postVo.setLikeCount(likeCount);
        // 点赞状态
        LoginUser loginUser = userContext.getUser();
        if (loginUser != null) {
            boolean likeStatus = likeService.queryPostLikeStatus(postId, loginUser.getUserId());
            postVo.setLiked(likeStatus);
        }
        return CommonResult.success(postVo);
//        List<CommentVo> commentVos = commentService.queryPostCommentList(postId, offset, limit, 5);

//        // 评论：给帖子的评论
//        // 回复：给评论的评论
//        // 评论列表
//        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), 0, 5);
//        // 评论VO列表
//        List<Map<String, Object>> commentVoList = new ArrayList<>();
//        if (commentList != null) {
//            for (Comment comment : commentList) {
//                // 评论VO
//                Map<String, Object> commentVo = new HashMap<>();
//                // 评论
//                commentVo.put("comment", comment);
//                // 作者
//                commentVo.put("user", userService.findUserById(comment.getUserId()));
//                // 点赞数量
//                long commentLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
//                commentVo.put("likeCount", commentLikeCount);
//                // 点赞状态
//                boolean commentLikeStatus = hostHolder.getUser() != null && likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
//                commentVo.put("likeStatus", commentLikeStatus);
//                // 回复列表
//                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
//                List<Map<String, Object>> replyVoList = new ArrayList<>();
//                if (replyList != null) {
//                    for (Comment reply : replyList) {
//                        Map<String, Object> replyVo = new HashMap<>();
//                        replyVo.put("reply", reply);
//                        replyVo.put("user", userService.findUserById(reply.getUserId()));
//                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
//                        replyVo.put("target", target);
//
//                        // 点赞数量
//                        commentLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
//                        replyVo.put("likeCount", commentLikeCount);
//                        // 点赞状态
//                        commentLikeStatus = hostHolder.getUser() != null && likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
//                        replyVo.put("likeStatus", commentLikeStatus);
//                        replyVoList.add(replyVo);
//                    }
//                }
//                commentVo.put("replys", replyVoList);
//
//                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
//                commentVo.put("replyCount", replyCount);
//                commentVoList.add(commentVo);
//
//            }
//        }
//
//        System.out.println(JSON.toJSONString(commentVoList));
    }

}
