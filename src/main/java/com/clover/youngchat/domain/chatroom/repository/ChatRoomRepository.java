package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ChatRoom.class, idClass = Long.class)
public interface ChatRoomRepository {

    ChatRoom save(ChatRoom chatRoom);
}
