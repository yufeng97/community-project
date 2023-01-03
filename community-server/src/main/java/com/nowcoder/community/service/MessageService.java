package com.nowcoder.community.service;

import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.entity.ChatRoom;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.MessageMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.SensitiveFilter;
import com.nowcoder.community.vo.ChatRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Service
public class MessageService {

    @Autowired
    private UserContext userContext;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<ChatRoomVo> queryConversationsByUserId(Integer userId, Date updateTime) {
        List<ChatRoom> chatRooms = messageMapper.selectConversationsByUserId(userId, updateTime);
        Set<Integer> userIds = new HashSet<>();
        for (ChatRoom room : chatRooms) {
            userIds.add(room.getUser1Id());
            userIds.add(room.getUser2Id());
        }
        Map<Integer, User> userMap = userMapper.selectByListId(userIds);
        List<ChatRoomVo> chatRoomVos = new ArrayList<>();
        for (ChatRoom room : chatRooms) {
            ChatRoomVo chatRoomVo = new ChatRoomVo(room);
            // 找出私信对象的userId
            Integer targetUserId = userId.equals(room.getUser1Id()) ? room.getUser2Id() : room.getUser1Id();
            chatRoomVo.setUser(userMap.get(targetUserId));
            chatRoomVos.add(chatRoomVo);
        }
        return chatRoomVos;
    }

    public long queryConversationsCount(Integer userId, Date updateTime) {
        return messageMapper.selectConversationsCount(userId, updateTime);
    }

    public List<Message> queryMessagesByTalkerId(Integer userId, Integer talkerId, Date createTime) {
        return messageMapper.selectMessageByUserId(userId, talkerId, createTime).stream().filter(
                message -> (message.getSenderId() == userId && message.getSenderDeleted() == 0)
                        || (message.getRecipientId() == userId && message.getRecipientDeleted() == 0)
        ).toList();
    }

    public int queryMessageCount(Integer userId, Integer talkerId, Date createTime) {
        return messageMapper.selectMessageCount(userId, talkerId, createTime);
    }

    public ChatRoom queryChatRoomById(Integer chatId) {
        return messageMapper.selectChatRoomById(chatId);
    }

    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }
}
