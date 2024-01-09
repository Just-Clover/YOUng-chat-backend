package com.clover.youngchat.domain.user.repository;

import com.clover.youngchat.domain.user.entity.User;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository {

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    User save(User user);

    Boolean existsByEmail(String email);


}
