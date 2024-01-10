package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatUser;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ChatUser.class, idClass = Long.class)
public interface ChatUserRepository {

    ChatUser save(ChatUser chatUser);

    Boolean existsByChatRoom_IdAndUser_Id(Long chatRoomId, Long userId);

    Optional<ChatUser> findByChatRoom_IdAndUser_Id(Long chatRoomId, Long userId);

    void delete(ChatUser chatUser);
}
