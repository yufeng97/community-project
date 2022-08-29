package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.vo.CommentVo;
import com.nowcoder.community.vo.PostCommentVo;
import com.nowcoder.community.vo.ReplyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCommentCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    List<PostCommentVo> selectPostCommentsById(int entityId);

    CommentVo selectCommentById(int id);

    ReplyVo selectReply(int id);

    List<CommentVo> selectCommentListByPostId(int postId, int offset, int limit, int replyLimit);


    List<ReplyVo> selectReplyListByCommentId(int commentId);

    @Select("select count(*) from comment where post_id = #{postId}")
    long selectCommentCountByPostId(int postId);
}
