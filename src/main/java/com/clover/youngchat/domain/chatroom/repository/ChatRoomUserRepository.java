package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ChatRoomUser.class, idClass = Long.class)
public interface ChatRoomUserRepository extends ChatRoomUserRepositoryCustom {

    ChatRoomUser save(ChatRoomUser chatRoomUser);

    boolean existsByChatRoom_IdAndUser_Id(Long chatRoomId, Long userId);

    Optional<ChatRoomUser> findByChatRoom_IdAndUser_Id(Long chatRoomId, Long userId);

    Optional<List<ChatRoomUser>> findByUser_Id(Long userId);

    void delete(ChatRoomUser chatRoomUser);

    List<ChatRoomUser> saveAll(List<ChatRoomUser> chatRoomUsers);
}
