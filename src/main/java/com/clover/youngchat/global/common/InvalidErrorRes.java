package com.clover.youngchat.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InvalidErrorRes {

    private final String field;
    private final String message;

    @Builder
    private InvalidErrorRes(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
