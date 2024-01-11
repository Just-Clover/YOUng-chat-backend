package com.clover.youngchat.domain.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.config.QueryDslConfig;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import test.ChatTest;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatRepositoryTest implements ChatTest {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Chat chat;
    private User user;
    private ChatRoom chatRoom;

    @BeforeEach
    void setup() {
        user = userRepository.save(TEST_USER);
        chatRoom = chatRoomRepository.save(TEST_CHAT_ROOM);

        chat = Chat.builder()
            .message(TEST_CHAT_MESSAGE)
            .sender(user)
            .chatRoom(chatRoom)
            .build();
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // when
        Chat saveChat = chatRepository.save(chat);

        // then
        assertThat(saveChat).isEqualTo(chat);
    }

    @Test
    @DisplayName("id로 채팅 조회 테스트")
    void findByIdTest() {
        // given
        chatRepository.save(chat);
        Long saveChatId = (Long) ReflectionTestUtils.getField(chat, "id");

        // when
        Optional<Chat> foundChat = chatRepository.findById(saveChatId);

        // then
        assertThat(foundChat).isPresent();
        assertThat(foundChat.get().getMessage()).isEqualTo(TEST_CHAT_MESSAGE);
        assertThat(foundChat.get().getSender()).isEqualTo(user);
        assertThat(foundChat.get().getChatRoom()).isEqualTo(chatRoom);
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        // given
        Chat saveChat = chatRepository.save(chat);
        Long saveChatId = (Long) ReflectionTestUtils.getField(chat, "id");

        // when - then
        chatRepository.delete(saveChat);
        Optional<Chat> foundChat = chatRepository.findById(saveChatId);

        // then
        assertThat(foundChat).isEmpty();
    }

}