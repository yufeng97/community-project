package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.Map;

@Mapper
public interface UserMapper {


    User selectById(int id);

    @MapKey("id")
    Map<Integer, User> selectByListId(Collection<Integer> ids);

    User selectByUsername(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
