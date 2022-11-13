package com.nowcoder.community;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.vo.CommentVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void commentTest1() {
        List<CommentVo> commentVos = commentService.queryPostCommentList(274, 0, 10, 3);
        System.out.println(JSONObject.toJSONString(commentVos, SerializerFeature.DisableCircularReferenceDetect));
        for (CommentVo commentVo : commentVos) {
            System.out.println(JSONObject.toJSONString(commentVo, SerializerFeature.DisableCircularReferenceDetect));
        }
    }

}
