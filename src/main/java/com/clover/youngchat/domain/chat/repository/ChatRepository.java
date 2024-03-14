package com.clover.youngchat.domain.chat.repository;

import com.clover.youngchat.domain.chat.entity.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {

    Chat findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    Optional<List<Chat>> findAllByChatRoomId(Long chatRoomId);
}
