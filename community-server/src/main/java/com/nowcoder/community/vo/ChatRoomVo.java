package com.nowcoder.community.vo;

import com.nowcoder.community.entity.ChatRoom;
import com.nowcoder.community.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ChatRoomVo {
    private Integer id;
    private User user;
    private String lastMessage;
    private Date updateTime;

    public ChatRoomVo(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.lastMessage = chatRoom.getLastMessage();
        this.updateTime = chatRoom.getUpdateTime();
    }
}
