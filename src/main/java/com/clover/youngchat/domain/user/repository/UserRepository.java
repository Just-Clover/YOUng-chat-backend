package com.clover.youngchat.domain.user.repository;

import com.clover.youngchat.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository {

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    Optional<List<User>> findUsersByIdIn(List<Long> userId);

    User save(User user);

    Boolean existsByEmail(String email);


}
