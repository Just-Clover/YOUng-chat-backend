package com.clover.youngchat.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
    // 성공 0번대
    SUCCESS(HttpStatus.OK, 0, "정상 처리되었습니다."),

    // 글로벌 1000번대
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1000, "알 수 없는 에러가 발생했습니다."),
    ACCESS_DENY(HttpStatus.FORBIDDEN, 1001, "접근 권한이 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 1002, "입력값이 올바르지 않습니다."),

    // 유저 2000번대
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, 2000, "중복된 email이 존재합니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 사용자 입니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, 2002, "기존 비밀번호가 일치하지 않습니다."),
    MISMATCH_CONFIRM_PASSWORD(HttpStatus.BAD_REQUEST, 2003, "변경된 비밀번호와 확인 비밀번호가 일치하지 않습니다."),
    SAME_OLD_PASSWORD(HttpStatus.BAD_REQUEST, 2004, "이전 비밀번호와 같습니다.");

    // 채팅방 3000번대

    // 채팅 4000번대

    private final HttpStatus status;
    private final Integer code;
    private final String message;

}
