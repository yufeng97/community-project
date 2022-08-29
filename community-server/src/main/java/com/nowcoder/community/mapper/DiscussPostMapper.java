package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.vo.PostInfo;
import com.nowcoder.community.vo.PostVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    List<DiscussPost> selectPostList(int userId);

    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且在<if>里使用，则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost post);

    @Select("select * from discuss_post where id=#{id}")
    DiscussPost selectDiscussPostById(int id);

    @Update("update discuss_post set comment_count = #{commentCount} where id = #{id}")
    int updateCommentCount(int id, int commentCount);

    List<PostInfo> selectPostInfoList(int userId);

    @Select("select * from discuss_post where id=#{id}")
    PostVo selectPostById(int id);
}
