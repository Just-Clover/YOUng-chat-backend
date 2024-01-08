package com.clover.youngchat.domain.sample.repository;

import com.clover.youngchat.domain.sample.entity.Sample;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Sample.class, idClass = Long.class)
public interface SampleRepository {

    Optional<Sample> findById(Long id);

    Sample save(Sample sample);

//    보류
//    default Sample findByIdOfNull(Long id) {
//        return findById(id).orElseThrow(NullPointerException::new);
//    }

}
