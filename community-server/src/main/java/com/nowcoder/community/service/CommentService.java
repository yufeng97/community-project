package com.nowcoder.community.service;

import com.nowcoder.community.dto.CommentDto;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Comment2;
import com.nowcoder.community.entity.Reply;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.exception.IllegalArgumentException;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.SensitiveFilter;
import com.nowcoder.community.vo.CommentVo;
import com.nowcoder.community.vo.ReplyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Service
@Slf4j
public class CommentService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getParentId() == null) {
            int postId = comment.getPostId();
            long count = queryPostCommentCount(postId);
            log.info("update post: {} count_count: {}", postId, count);
            discussPostService.updateCommentCount(comment.getPostId(), count);
        } else {
            Integer commentId = comment.getParentId();
            long count = queryCommentReplyCount(commentId);
            log.info("update comment: {} reply_count: {}", commentId, count);
            commentMapper.updateReplyCount(commentId, count);
        }

        return rows;
    }

//    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
//    public int addReply(Reply reply) {
//        if (reply == null) {
//            throw new IllegalArgumentException();
//        }
//        reply.setContent(HtmlUtils.htmlEscape(reply.getContent()));
//        reply.setContent(sensitiveFilter.filter(reply.getContent()));
//        int rows = commentMapper.insertReply(reply);
//
//        // 更新评论回复数量
//        long count = queryCommentReplyCount(reply.getCommentId());
//        commentMapper.updateReplyCount(reply.getId(), count);
//        return rows;
//    }

    public List<CommentVo> queryPostCommentList(int postId, int offset, int limit, int replyLimit) {
        List<CommentVo> commentVos = commentMapper.selectCommentListByPostId(postId, offset, limit, replyLimit);

//        if (!commentVos.isEmpty()) {
//            Set<Integer> userIdSet = new HashSet<>();
//            for (CommentVo commentVo : commentVos) {
//                userIdSet.add(commentVo.getAuthor().getId());
//                for (ReplyVo replyVo : commentVo.getReplies()) {
//                    userIdSet.add(replyVo.getAuthor().getId());
//                    if (replyVo.getTarget() != null) {
//                        userIdSet.add(replyVo.getTarget().getId());
//                    }
//                }
//            }
//            Map<Integer, User> userMap = userMapper.selectByListId(userIdSet);
//            for (CommentVo commentVo : commentVos) {
//                User commentAuthor = userMap.get(commentVo.getAuthor().getId());
//                commentVo.setAuthor(commentAuthor);
//                for (ReplyVo replyVo : commentVo.getReplies()) {
//                    User replyAuthor = userMap.get(replyVo.getAuthor().getId());
//                    replyVo.setAuthor(replyAuthor);
//                    if (replyVo.getTarget() != null) {
//                        User replyTarget = userMap.get(replyVo.getTarget().getId());
//                        replyVo.setTarget(replyTarget);
//                    }
//                }
//            }
//        }

        if (!commentVos.isEmpty()) {
            Set<Integer> userIdSet = new HashSet<>();
            for (CommentVo commentVo : commentVos) {
                userIdSet.add(commentVo.getAuthor().getId());
                for (CommentVo replyVo : commentVo.getReplies()) {
                    userIdSet.add(replyVo.getAuthor().getId());
                    if (replyVo.getTarget() != null) {
                        userIdSet.add(replyVo.getTarget().getId());
                    }
                }
            }
            Map<Integer, User> userMap = userMapper.selectByListId(userIdSet);
            for (CommentVo commentVo : commentVos) {
                User commentAuthor = userMap.get(commentVo.getAuthor().getId());
                commentVo.setAuthor(commentAuthor);
                for (CommentVo replyVo : commentVo.getReplies()) {
                    User replyAuthor = userMap.get(replyVo.getAuthor().getId());
                    replyVo.setAuthor(replyAuthor);
                    if (replyVo.getTarget() != null) {
                        User replyTarget = userMap.get(replyVo.getTarget().getId());
                        replyVo.setTarget(replyTarget);
                    }
                }
            }
        }

        return commentVos;
    }

    public long queryPostCommentCount(int postId) {
        return commentMapper.selectCommentCountByPostId(postId);
    }

    public long queryCommentReplyCount(int commentId) {
        return commentMapper.selectReplyCountByCommentId(commentId);
    }

    public List<CommentVo> queryCommentReplyList(int commentId) {
        List<CommentVo> replyVos = commentMapper.selectReplyListByCommentId(commentId);
        if (!replyVos.isEmpty()) {
            Set<Integer> userIdSet = new HashSet<>();
            for (CommentVo replyVo : replyVos) {
                userIdSet.add(replyVo.getAuthor().getId());
                if (replyVo.getTarget() != null) {
                    userIdSet.add(replyVo.getTarget().getId());
                }
            }
            Map<Integer, User> userMap = userMapper.selectByListId(userIdSet);
            for (CommentVo replyVo : replyVos) {
                User replyAuthor = userMap.get(replyVo.getAuthor().getId());
                replyVo.setAuthor(replyAuthor);
                if (replyVo.getTarget() != null) {
                    User replyTarget = userMap.get(replyVo.getTarget().getId());
                    replyVo.setTarget(replyTarget);
                }
            }
        }
        return replyVos;
    }

    public boolean checkCommentExistById(int id) {
        return commentMapper.checkCommentExistById(id);
    }

}
