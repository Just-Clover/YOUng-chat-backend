package com.clover.youngchat.domain.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import test.ChatTest;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatRepositoryTest implements ChatTest {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // given
        User user = userRepository.save(TEST_USER);
        ChatRoom chatRoom = chatRoomRepository.save(TEST_CHAT_ROOM);
        Chat chat = Chat.builder()
            .message(TEST_CHAT_MESSAGE)
            .sender(user)
            .chatRoom(chatRoom)
            .build();

        // when
        Chat saveChat = chatRepository.save(chat);

        // then
        assertThat(saveChat).isEqualTo(chat);
    }

}