package com.nowcoder.community.service;

import com.github.pagehelper.PageHelper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Pagination;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.SensitiveFilter;
import com.nowcoder.community.vo.PostInfo;
import com.nowcoder.community.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DiscussPostService {

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public List<DiscussPost> queryPostList(Pagination pagination) {
        PageHelper.startPage(pagination);

        return discussPostMapper.selectPostList(0);
    }

    public List<PostInfo> queryPostInfoList(Pagination pagination) {
        PageHelper.startPage(pagination);
        List<PostInfo> postInfos = discussPostMapper.selectPostInfoList(0);
        List<Integer> userIdList = postInfos.stream().map(PostInfo::getUserId).distinct().toList();
        if (!userIdList.isEmpty()) {
            Map<Integer, User> userMap = userMapper.selectByListId(userIdList);
            for (PostInfo postInfo : postInfos) {
                postInfo.setAuthor(userMap.get(postInfo.getUserId()));
            }
        }

        return postInfos;
    }

    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public PostVo queryPostById(int id) {
        PostVo postVo = discussPostMapper.selectPostById(id);
        System.out.println(postVo);
        System.out.println(postVo.getUserId());
        User user = userMapper.selectById(postVo.getUserId());
        postVo.setAuthor(user);
        System.out.println(postVo);
        return postVo;
    }

    public int updateCommentCount(int id, long commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    public boolean checkPostExistById(int id) {
        return discussPostMapper.checkPostExistById(id);
    }
}
