package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatUser;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ChatUser.class, idClass = Long.class)
public interface ChatUserRepository {

    ChatUser save(ChatUser chatUser);
}
