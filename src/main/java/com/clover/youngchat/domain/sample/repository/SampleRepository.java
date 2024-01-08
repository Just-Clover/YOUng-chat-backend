package com.clover.youngchat.domain.sample.repository;

import com.clover.youngchat.domain.sample.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {

}
