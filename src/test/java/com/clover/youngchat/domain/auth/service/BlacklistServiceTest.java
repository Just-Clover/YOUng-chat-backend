package com.clover.youngchat.domain.auth.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clover.youngchat.domain.auth.entity.Blacklist;
import com.clover.youngchat.domain.auth.repository.BlacklistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.BlacklistTest;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceTest implements BlacklistTest {

    @Mock
    private BlacklistRepository blacklistRepository;

    @InjectMocks
    private BlacklistService blacklistService;

    @Test
    @DisplayName("블랙리스트 토큰 추가 : 성공")
    public void addTokenBlacklistSuccess() {
        // when
        blacklistService.addTokenToBlacklist(TEST_ACCESS_TOKEN, TEST_EXPIRATION);

        // then
        verify(blacklistRepository, times(1)).save(any(Blacklist.class));
    }
}