package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ChatRoomUser.class, idClass = Long.class)
public interface ChatRoomUserRepository extends ChatRoomUserRepositoryCustom {

    ChatRoomUser save(ChatRoomUser chatRoomUser);

    boolean existsByChatRoom_IdAndUser_Id(Long chatRoomId, Long userId);

    Optional<ChatRoomUser> findByChatRoom_IdAndUser_Id(Long chatRoomId, Long userId);

    Optional<List<ChatRoomUser>> findByUser_Id(Long userId);

    void delete(ChatRoomUser chatRoomUser);

    List<ChatRoomUser> saveAll(Iterable<ChatRoomUser> chatRoomUsers);

    @Query("SELECT c FROM ChatRoom c "
        + "JOIN ChatRoomUser cu ON c.id = cu.chatRoom.id "
        + "GROUP BY c.id "
        + "HAVING COUNT(cu.chatRoom.id) = 2 AND "
        + "SUM(CASE WHEN cu.user.id IN (:userId, :friendId) THEN 1 ELSE 0 END) = 2")
    Optional<ChatRoom> findChatRoomIdByOnlyTwoUsers(Long userId, Long friendId);
}
