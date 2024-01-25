package com.clover.youngchat.domain.chat.entity;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.model.BaseEntity;
import com.clover.youngchat.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isDeleted = false;
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false, updatable = false)
    private ChatRoom chatRoom;

    // TODO: senderName 따로뺐을때 성능차이 어떻게 나는지 확인해보기

    @Builder
    private Chat(String message, User sender, ChatRoom chatRoom) {
        this.message = message;
        this.sender = sender;
        this.chatRoom = chatRoom;
    }

    public void deleteChat() {
        this.isDeleted = true;
    }
}
