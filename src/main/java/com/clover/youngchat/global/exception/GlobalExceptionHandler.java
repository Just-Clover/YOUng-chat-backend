package com.clover.youngchat.global.exception;

import com.clover.youngchat.global.common.ErrorRes;
import com.clover.youngchat.global.common.InvalidErrorRes;
import com.clover.youngchat.global.response.RestResponse;
import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public RestResponse<ErrorRes> handleGlobalException(GlobalException e) {
        return RestResponse.error(e.getResultCode(), new ErrorRes());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse<List<InvalidErrorRes>> handleInputInvalidException(
        MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        List<InvalidErrorRes> errorResList = errors.stream()
            .map(e -> InvalidErrorRes.builder()
                .field(e.getField())
                .message(e.getDefaultMessage())
                .build()
            ).toList();

        return RestResponse.error(ResultCode.INVALID_INPUT, errorResList);
    }
}
