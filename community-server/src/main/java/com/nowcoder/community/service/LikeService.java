package com.nowcoder.community.service;

import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.LikeMapper;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void postLike(int userId, int postId) {
//        String postLikeKey = RedisKeyUtil.getPostLikeKey(postId);
        boolean recordExist = likeMapper.checkPostLikeRecordExists(postId, userId);
        if (!recordExist) {
            likeMapper.insertPostLikeRecord(postId, userId, 1);
        } else {
            boolean status = likeMapper.selectPostLikeStatus(postId, userId);
            likeMapper.updatePostLikeRecordStatus(postId, userId, !status);
        }
        long likeCount = likeMapper.selectPostLikeCount(postId);
        discussPostMapper.updatePostLikeCount(postId, likeCount);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void commentLike(int userId, int commentId) {
        boolean recordExist = likeMapper.checkCommentLikeRecordExists(commentId, userId);
        if (!recordExist) {
            likeMapper.insertCommentLikeRecord(commentId, userId, 1);
        } else {
            boolean status = likeMapper.selectCommentLikeStatus(commentId, userId);
            likeMapper.updateCommentLikeRecordStatus(commentId, userId, !status);
        }
        long likeCount = likeMapper.selectCommentLikeCount(commentId);
        commentMapper.updateCommentLikeCount(commentId, likeCount);
    }

    public void like(int userId, int entityType, int entityId, int entityUserId) {
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if (isMember) {
//            redisTemplate.opsForSet().remove(entityLikeKey, userId);
//        } else {
//            redisTemplate.opsForSet().add(entityLikeKey, userId);
//        }

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
    }

    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    public Boolean findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId);
    }

    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }

    public boolean queryCommentLikeStatus(int commentId, int userId) {
        return likeMapper.selectCommentLikeStatus(commentId, userId);
    }

    public boolean queryPostLikeStatus(int postId, int userId) {
        return likeMapper.selectPostLikeStatus(postId, userId);
    }

}
