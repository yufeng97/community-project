package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.vo.PostInfo;
import com.nowcoder.community.vo.PostVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且在<if>里使用，则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    List<PostInfo> selectPostInfoList(int userId);

    @Select("select * from discuss_post where id=#{id}")
    PostVo selectPostById(int id);

    @Select("select exists(select 1 from discuss_post where id=#{id} and status!=2)")
    boolean checkPostExistById(int id);

//    @Insert("insert into discuss_post (user_id, title, content, type, status, create_time, comment_count, score)" +
//            "   values (#{userId}, #{title}, #{content}, #{type}, #{status}, #{createTime}, #{commentCount}, #{score})")
    int insertDiscussPost(DiscussPost post);

    @Update("update discuss_post set comment_count = #{commentCount} where id = #{id}")
    int updateCommentCount(int id, long commentCount);

    @Update("update discuss_post set like_count=#{count} where id=#{id}")
    int updatePostLikeCount(int id, long count);
}
