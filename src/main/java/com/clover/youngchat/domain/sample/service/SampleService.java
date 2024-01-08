package com.clover.youngchat.domain.sample.service;

import com.clover.youngchat.domain.sample.dto.request.SampleSaveReq;
import com.clover.youngchat.domain.sample.dto.response.SampleGetRes;
import com.clover.youngchat.domain.sample.dto.response.SampleSaveRes;
import com.clover.youngchat.domain.sample.entity.Sample;
import com.clover.youngchat.domain.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    public SampleGetRes getSample(Long sampleId) {
        Sample sample = sampleRepository.findById(sampleId).orElseThrow(NullPointerException::new);

        return SampleGetRes.to(sample);
    }

    public SampleSaveRes saveSample(SampleSaveReq sampleSaveReq) {
        Sample sample = Sample.builder()
            .title(sampleSaveReq.getTitle())
            .text(sampleSaveReq.getText())
            .build();

        sampleRepository.save(sample);

        return new SampleSaveRes();
    }
}
