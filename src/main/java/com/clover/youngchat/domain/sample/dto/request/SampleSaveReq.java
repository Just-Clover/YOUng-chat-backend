package com.clover.youngchat.domain.sample.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleSaveReq {

    private String title;
    private String text;

    @Builder
    private SampleSaveReq(String title, String text) {
        this.title = title;
        this.text = text;
    }
}