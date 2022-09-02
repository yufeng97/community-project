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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Service
public class CommentService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCommentCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCommentCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment2 comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        long count = queryPostCommentCount(comment.getPostId());
        discussPostService.updateCommentCount(comment.getPostId(), count);
        return rows;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addReply(Reply reply) {
        if (reply == null) {
            throw new IllegalArgumentException();
        }
        reply.setContent(HtmlUtils.htmlEscape(reply.getContent()));
        reply.setContent(sensitiveFilter.filter(reply.getContent()));
        int rows = commentMapper.insertReply(reply);

        // 更新评论回复数量
        long count = queryCommentReplyCount(reply.getCommentId());
        commentMapper.updateReplyCount(reply.getId(), count);
        return rows;
    }

    public List<CommentVo> queryPostCommentList(int postId, int offset, int limit, int replyLimit) {
        List<CommentVo> commentVos = commentMapper.selectCommentListByPostId(postId, offset, limit, replyLimit);
        System.out.println(commentVos);

        if (!commentVos.isEmpty()) {
            Set<Integer> userIdSet = new HashSet<>();
            for (CommentVo commentVo : commentVos) {
                userIdSet.add(commentVo.getAuthor().getId());
                for (ReplyVo replyVo : commentVo.getReplies()) {
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
                for (ReplyVo replyVo : commentVo.getReplies()) {
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

    public List<ReplyVo> queryCommentReplyList(int commentId) {
        List<ReplyVo> replyVos = commentMapper.selectReplyListByCommentId(commentId);
        if (!replyVos.isEmpty()) {
            Set<Integer> userIdSet = new HashSet<>();
            for (ReplyVo replyVo : replyVos) {
                userIdSet.add(replyVo.getAuthor().getId());
                if (replyVo.getTarget() != null) {
                    userIdSet.add(replyVo.getTarget().getId());
                }
            }
            Map<Integer, User> userMap = userMapper.selectByListId(userIdSet);
            for (ReplyVo replyVo : replyVos) {
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
