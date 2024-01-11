package com.clover.youngchat.domain.chat.repository;

import com.clover.youngchat.domain.chat.entity.Chat;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Chat.class, idClass = Long.class)
public interface ChatRepository {

    Chat save(Chat chat);

    @Query("select c from Chat c where c.chatRoom.id = :chatRoomId order by c.createdAt desc limit 1")
    Optional<Chat> findByChatRoom_Id(@Param("chatRoomId") Long chatRoomId);

    Optional<List<Chat>> findAllByChatRoom_Id(Long chatRoomId);
}
