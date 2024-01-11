package com.clover.youngchat.domain.chat.repository;

import com.clover.youngchat.domain.chat.entity.Chat;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Chat.class, idClass = Long.class)
public interface ChatRepository {

    Chat save(Chat chat);
}
