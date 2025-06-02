package navi4.zipsa.command.user.exception;

public class UserExceptionMessages {
    public static final String INVALID_SIGNUP_LOGIN_ID = "아이디는 중복되지 않아야 하며, 영문자와 숫자 조합 5~15자여야 합니다.";
    public static final String INVALID_SIGNUP_PASSWORD = "비밀번호는 8자~16자, 영문자/숫자/특수문자를 각각 하나 이상 포함해야 합니다.";
    public static final String INVALID_SIGNUP_USERNAME = "사용자 이름은 2~10자여야 합니다.";
    public static final String DUPLICATED_LOGIN_ID = "는 이미 존재하는 아이디입니다.";
    public static final String NOT_EXIST_LOGIN_ID = "존재하지 않는 아이디입니다.";
    public static final String WRONG_PASSWORD = "비밀번호가 일치하지 않습니다.";
}
