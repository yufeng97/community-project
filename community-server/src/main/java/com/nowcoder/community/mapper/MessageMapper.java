package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.ChatRoom;
import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("select * from private_chat_list where (user1_id=#{userId} or user2_id=#{userId}) and update_time < #{updateTime} order by update_time desc limit 5")
    List<ChatRoom> selectConversationsByUserId(int userId, Date updateTime);

    @Select("select count(*) from private_chat_list where (user1_id=#{userId} or user2_id=#{userId}) and update_time < #{updateTime}")
    long selectConversationsCount(int userId, Date updateTime);

    @Select("select * from private_chat_list where id=#{chatId}")
    ChatRoom selectChatRoomById(Integer chatId);

    @Select("select * from private_message " +
            "where ((sender_id=#{senderId} and recipient_id=#{recipientId}) or (sender_id=#{recipientId} and recipient_id=#{senderId})) " +
            "and create_time < #{createTime} " +
            "and sender_deleted=0 " +
            "order by create_time desc limit 10")
    List<Message> selectMessageByUserId(Integer senderId, Integer recipientId, Date createTime);

    @Select("select count(*) from private_message " +
            "where ((sender_id=#{senderId} and recipient_id=#{recipientId}) or (sender_id=#{recipientId} and recipient_id=#{senderId})) " +
            "and create_time < #{createTime} ")
    int selectMessageCount(Integer senderId, Integer recipientId, Date createTime);

    // 查询当前用户的会话列表，针对每个会话只返回一跳最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 插入消息
    int insertMessage(Message message);

    // 更新状态 0-未读, 1-已读, 2-删除
    int updateStatus(List<Integer> ids, int status);
}
