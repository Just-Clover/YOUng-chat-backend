package com.clover.youngchat.domain.auth.service;

import com.clover.youngchat.domain.auth.entity.Blacklist;
import com.clover.youngchat.domain.auth.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;

    public void addTokenToBlacklist(String accessToken) {
        Blacklist blacklist = Blacklist.builder()
            .accessToken(accessToken)
            .build();
        blacklistRepository.save(blacklist);
    }

    public boolean isTokenBlackListed(String accessToken) {
        return blacklistRepository.existsById(accessToken);
    }

} 
