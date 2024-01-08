package com.clover.youngchat.domain.user.repository;

import com.clover.youngchat.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository {

    Optional<User> findById(Long userId);

    Boolean existsByEmail(String email);

    User save(User user);
}
