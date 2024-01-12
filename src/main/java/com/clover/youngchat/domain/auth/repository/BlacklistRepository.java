package com.clover.youngchat.domain.auth.repository;

import com.clover.youngchat.domain.auth.entity.Blacklist;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Blacklist.class, idClass = String.class)
public interface BlacklistRepository {

    Blacklist save(Blacklist blacklist);

    boolean existsById(String accessToken);
}
