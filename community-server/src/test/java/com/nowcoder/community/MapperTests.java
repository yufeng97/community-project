package com.nowcoder.community;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.CommentLikeRecord;
import com.nowcoder.community.entity.PostLikeRecord;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.LikeMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.vo.CommentVo;
import com.nowcoder.community.vo.PostCommentVo;
import com.nowcoder.community.vo.PostInfo;
import com.nowcoder.community.vo.PostVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Test
    public void userTest1() {
        Map<Integer, User> userMap = userMapper.selectByListId(Arrays.asList(101, 102, 103));
        System.out.println(userMap);

        User user = userMapper.selectById(101);
        System.out.println(user);
    }

    @Test
    public void postTest1() {
        PostVo postVo = discussPostMapper.selectPostById(274);
//        PostInfo postVo = discussPostMapper.selectPostById(274);
        System.out.println(postVo);
        System.out.println("JSONObject.toJSONString(postVo) = " + JSONObject.toJSONString(postVo));
    }

    @Test
    public void commentTest1() {
        List<CommentVo> commentVos = commentMapper.selectCommentListByPostId(274, 0, 10, 5);
        for (CommentVo vo : commentVos) {
            System.out.println(vo);
        }
    }

    @Test
    public void commentTest2() {
        List<PostCommentVo> postCommentVos = commentMapper.selectPostCommentsById(247);
        System.out.println(postCommentVos);
    }

    @Test
    public void postInfoTest1() {
        List<PostInfo> postInfos = discussPostMapper.selectPostInfoList(0);
        System.out.println(JSONObject.toJSONString(postInfos));
        List<Integer> userIds = postInfos.stream().map(PostInfo::getUserId).distinct().toList();
        System.out.println(userIds);

        Map<Integer, User> userMap = userMapper.selectByListId(userIds);
        System.out.println(userMap);

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
        for (PostInfo postInfo : postInfos) {
            postInfo.setAuthor(userMap.get(postInfo.getUserId()));
        }
        for (PostInfo postInfo : postInfos) {
            System.out.println(postInfo);
        }
    }

    @Test
    public void postTest2() {
        boolean exist = discussPostMapper.checkPostExistById(285);
        System.out.println(exist);
    }

    @Test
    public void likeTest2() {
        Map<Integer, PostLikeRecord> map = likeMapper.selectPostLikeStatusByListId(Arrays.asList(273, 274, 275), 111);
        System.out.println(map);
    }

    @Test
    public void likeTest1() {
        Map<Integer, CommentLikeRecord> map = likeMapper.selectCommentLikeStatusByListId(Arrays.asList(104, 105, 106, 111), 111);
        System.out.println(map);

        boolean status = likeMapper.selectCommentLikeStatus(113, 111);
        System.out.println(status);
        likeMapper.updateCommentLikeRecordStatus(113, 111, !status);
        status = likeMapper.selectCommentLikeStatus(113, 111);
        System.out.println(status);
    }

}
