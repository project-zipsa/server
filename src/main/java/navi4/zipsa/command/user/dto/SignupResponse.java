package navi4.zipsa.command.user.dto;

import navi4.zipsa.command.user.domain.User;

public record SignupResponse(
        String loginId,
        String password,
        String username
) {
    public static SignupResponse from(User user) {
        return new SignupResponse(user.getLoginId(), user.getPassword(), user.getUserName());
    }
}
