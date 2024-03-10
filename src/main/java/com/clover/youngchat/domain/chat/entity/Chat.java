package com.clover.youngchat.domain.chat.entity;

import com.clover.youngchat.domain.model.MongoBaseEntity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends MongoBaseEntity {

    @Id
    private String id;
    private String message;
    private Long senderId;
    private Long chatRoomId;
    private boolean isDeleted = false;

    @Builder
    private Chat(String message, Long senderId, Long chatRoomId, boolean isDeleted) {
        this.message = message;
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
        this.isDeleted = isDeleted;
    }

    public void deleteChat() {
        this.isDeleted = true;
    }
}
