package com.clover.youngchat.global.response;

import com.clover.youngchat.global.exception.ResultCode;
import java.io.Serializable;
import lombok.Builder;
import org.springframework.http.HttpStatus;


@Builder
public class RestResponse<T> implements Serializable {

    private HttpStatus status;
    private Integer code;
    private String message;
    private T data;

    public static <T> RestResponse<T> success(T data) {
        return RestResponse.<T>builder()
            .status(ResultCode.SUCCESS.getStatus())
            .code(ResultCode.SUCCESS.getCode())
            .message(ResultCode.SUCCESS.getMessage())
            .data(data)
            .build();
    }

    public static <T> RestResponse<T> error(ResultCode resultCode, T data) {
        return RestResponse.<T>builder()
            .status(resultCode.getStatus())
            .code(resultCode.getCode())
            .message(resultCode.getMessage())
            .data(data)
            .build();
    }
}