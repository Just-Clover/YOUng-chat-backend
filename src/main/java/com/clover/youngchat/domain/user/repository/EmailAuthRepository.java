package com.clover.youngchat.domain.user.repository;

import com.clover.youngchat.domain.user.entity.EmailAuth;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = EmailAuth.class, idClass = Long.class)
public interface EmailAuthRepository {

    boolean existsById(String email);

    Optional<EmailAuth> findById(String email);

    void deleteById(String email);

    EmailAuth save(EmailAuth emailAuth);
}
