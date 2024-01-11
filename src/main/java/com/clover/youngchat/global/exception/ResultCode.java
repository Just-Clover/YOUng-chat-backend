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
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, 1003, "해당 파일을 찾을 수 없습니다."),
    INVALID_PROFILE_IMAGE_TYPE(HttpStatus.BAD_REQUEST, 1004, "지원하지 않는 파일 형식 입니다."),

    // 유저 2000번대
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, 2000, "중복된 email이 존재합니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 사용자 입니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, 2002, "기존 비밀번호가 일치하지 않습니다."),
    MISMATCH_CONFIRM_PASSWORD(HttpStatus.BAD_REQUEST, 2003, "변경된 비밀번호와 확인 비밀번호가 일치하지 않습니다."),
    SAME_OLD_PASSWORD(HttpStatus.BAD_REQUEST, 2004, "이전 비밀번호와 같습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, 2005, "해당 이메일을 찾을 수 없습니다."),
    EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 2006, "이메일 전송에 실패했습니다."),
    INVALID_CODE(HttpStatus.BAD_REQUEST, 2007, "인증번호 코드가 일치하지 않습니다"),
    UNAUTHORIZED_EMAIL(HttpStatus.BAD_REQUEST, 2008, "인증되지 않은 이메일입니다."),

    // 채팅방 3000번대
    NOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND, 3000, "존재하지 않는 채팅방 입니다."),
    NOT_MEMBER_CHATROOM(HttpStatus.NOT_FOUND, 3001, "채팅방의 멤버만 접근할 수 있습니다"),

    // 채팅 4000번대

    // 친구 5000번대
    NOT_FOUND_FRIEND(HttpStatus.NOT_FOUND, 5000, "친구를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final Integer code;
    private final String message;

}
