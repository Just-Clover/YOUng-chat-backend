package com.clover.youngchat.domain.auth.repository;

import com.clover.youngchat.domain.auth.entity.EmailAuth;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = EmailAuth.class, idClass = String.class)
public interface EmailAuthRepository {

    boolean existsById(String email);

    Optional<EmailAuth> findById(String email);

    void deleteById(String email);

    EmailAuth save(EmailAuth emailAuth);
}
