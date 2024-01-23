package com.clover.youngchat.domain.user.constant;

public class UserConstant {

    // 정규식
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String USERNAME_REGEX = "^[A-Za-z가-힣0-9]{2,10}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()_\\-+=?])[a-z\\d~!@#$%^&*()_\\-+=?]{8,15}$";

    // 메시지
    public static final String EMAIL_AUTHENTICATION = "YOUngChat! [이메일 인증]";
    public static final String EMAIL_MESSAGE = "올바른 이메일 형식이 아닙니다.";
    public static final String USERNAME_MESSAGE = "username은 영어 대소문자, 숫자, 한글 2글자 이상 10글자 이하입니다";
    public static final String PASSWORD_MESSAGE = "비밀번호는 영소문자, 특수문자, 숫자를 포함한 8글자 이상 15글자 이하입니다.";
}
