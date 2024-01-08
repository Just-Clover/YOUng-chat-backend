package com.clover.youngchat.domain.sample.dto.response;

import com.clover.youngchat.domain.sample.entity.Sample;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleGetRes {

    private Long sampleId;
    private String title;
    private String text;

    @Builder
    private SampleGetRes(Long sampleId, String title, String text) {
        this.sampleId = sampleId;
        this.title = title;
        this.text = text;
    }

    public static SampleGetRes to(Sample sample) {
        return SampleGetRes.builder()
            .sampleId(sample.getId())
            .title(sample.getTitle())
            .text(sample.getText())
            .build();
    }
}