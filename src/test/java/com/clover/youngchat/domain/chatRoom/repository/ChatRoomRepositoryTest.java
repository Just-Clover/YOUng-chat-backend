package com.clover.youngchat.domain.chatRoom.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static test.ChatRoomTest.TEST_CHAT_ROOM;
import static test.ChatRoomTest.TEST_CHAT_ROOM_TITLE;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        ChatRoom chatRoom = chatRoomRepository.save(TEST_CHAT_ROOM);

        assertThat(chatRoom.getTitle()).isEqualTo(TEST_CHAT_ROOM_TITLE);
    }
}
