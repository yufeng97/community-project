package com.nowcoder.community.mapper;

import com.nowcoder.community.dto.CommentDto;
import com.nowcoder.community.dto.ReplyDto;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Comment2;
import com.nowcoder.community.entity.Reply;
import com.nowcoder.community.vo.CommentVo;
import com.nowcoder.community.vo.PostCommentVo;
import com.nowcoder.community.vo.ReplyVo;
import org.apache.ibatis.annotations.*;

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

    @Select("select count(*) from comment where post_id = #{postId} and status != 2")
    long selectCommentCountByPostId(int postId);

    @Select("select count(*) from reply where comment_id=#{commentId} and status != 2")
    long selectReplyCountByCommentId(int commentId);

    @Select("select exists(select 1 from comment where id=#{id} and status != 2)")
    boolean checkCommentExistById(int id);

    @Insert("insert into comment(user_id, post_id, content, reply_count, status, create_time, update_time) " +
            "values (#{userId}, #{postId}, #{content}, 0, 0, now(), now())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insertComment(Comment2 comment);

    @Insert("insert into reply(comment_id, from_id, to_id, content, status, create_time, update_time) " +
            "values (#{commentId}, #{fromId}, #{toId}, #{content}, 0, now(), now())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insertReply(Reply reply);

    @Update("update comment set reply_count=#{replyCount} where id=#{id}")
    int updateReplyCount(int id, long replyCount);
}
