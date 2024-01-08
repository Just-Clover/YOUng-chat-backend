package com.clover.youngchat.domain.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.UserTest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공테스트")
    void signupSuccessTest() {
        // given
        UserSignupReq userSignupReq = UserSignupReq.builder()
            .email(TEST_USER_EMAIL)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();

        given(userRepository.existsByEmail(userSignupReq.getEmail())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(TEST_USER);

        // when
        userService.signup(userSignupReq);

        // then
        verify(passwordEncoder, times(1)).encode(TEST_USER_PASSWORD);
        verify(userRepository, times(1)).existsByEmail(TEST_USER_EMAIL);
        verify(userRepository).save(any(User.class));
    }

}