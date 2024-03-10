package com.clover.youngchat.domain.chat.repository;

import com.clover.youngchat.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {

//    @Query("select c from Chat c where c.chatRoom.id = :chatRoomId order by c.createdAt desc limit 1")
//    Optional<Chat> findLastChatByChatRoom_Id(@Param("chatRoomId") Long chatRoomId);

    //    Optional<List<Chat>> findAllByChatRoom_Id(Long chatRoomId);

    Chat findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
}
