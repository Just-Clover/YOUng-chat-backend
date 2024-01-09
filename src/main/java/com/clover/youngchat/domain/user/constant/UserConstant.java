package com.clover.youngchat.domain.user.constant;

public class UserConstant {

    // 정규식
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String USERNAME_REGEX = "^[a-z가-힣]{4,10}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$";

    // 메시지
    public static final String EMAIL_MESSAGE = "올바른 이메일 형식이 아닙니다.";
    public static final String USERNAME_MESSAGE = "username은 영소문자,한글 4글자 이상 10글자 이하입니다";
    public static final String PASSWORD_MESSAGE = "비밀번호는 영소문자, 대문자, 특수문자, 숫자를 포함한 8글자 이상 15글자 이하입니다.";
}
