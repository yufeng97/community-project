package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketMapper {

    @Insert({"insert into login_ticket(user_id, ticket, status, expired) values(#{userId}, #{ticket}, #{status}, #{expired})"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({"select id, user_id, ticket, status, expired from login_ticket where ticket=#{ticket} limit 1"})
    LoginTicket selectByTicket(String ticket);

    @Update({"<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\">",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);

    @Update("update login_ticket set expired=#{expired} where id=#{id}")
    int updateTicket(LoginTicket ticket);
}
