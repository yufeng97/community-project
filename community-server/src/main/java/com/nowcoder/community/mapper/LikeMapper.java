package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.CommentLikeRecord;
import com.nowcoder.community.entity.PostLikeRecord;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.Map;

@Mapper
public interface LikeMapper {

    @Select("select ifnull(sum(`status`), 0) from post_like_record where post_id=#{postId}")
    long selectPostLikeCount(int postId);

    @Select("select ifnull(sum(`status`), 0) from comment_like_record where comment_id=#{commentId}")
    long selectCommentLikeCount(int commentId);

    @MapKey("postId")
    Map<Integer, PostLikeRecord> selectPostLikeStatusByListId(Collection<Integer> ids, int userId);

    @MapKey("commentId")
    Map<Integer, CommentLikeRecord> selectCommentLikeStatusByListId(Collection<Integer> ids, int userId);

    @Insert("insert into post_like_record(post_id, user_id, status) VALUES (#{postId}, #{userId}, #{status})")
    int insertPostLikeRecord(int postId, int userId, int status);

    @Insert("insert into comment_like_record(comment_id, user_id, status) VALUES (#{commentId}, #{userId}, #{status})")
    int insertCommentLikeRecord(int commentId, int userId, int status);

    @Select("select exists(select 1 from post_like_record where post_id=#{postId} and user_id=#{userId})")
    boolean checkPostLikeRecordExists(int postId, int userId);

    @Select("select exists(select 1 from post_like_record where comment_id=#{commentId} and user_id=#{userId}")
    boolean checkCommentLikeRecordExists(int commentId, int userId);

    @Select("select coalesce(max(`status`), 0) from post_like_record where post_id=#{postId} and user_id=#{userId}")
    boolean selectPostLikeStatus(int postId, int userId);

    @Select("select coalesce(max(`status`), 0) from comment_like_record where comment_id=#{commentId} and user_id=#{userId}")
    boolean selectCommentLikeStatus(int commentId, int userId);

    @Update("update post_like_record set status=#{status} where post_id=#{postId} and user_id=#{userId}")
    int updatePostLikeRecordStatus(int postId, int userId, boolean status);

    @Update("update comment_like_record set status=#{status} where comment_id=#{commentId} and user_id=#{userId}")
    int updateCommentLikeRecordStatus(int commentId, int userId, boolean status);

}
