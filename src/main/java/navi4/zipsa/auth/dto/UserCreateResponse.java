package navi4.zipsa.auth.dto;

import navi4.zipsa.auth.domain.User;

public record UserCreateResponse(
        String loginId,
        String password,
        String name
) {
    public static UserCreateResponse from(User user) {
        return new UserCreateResponse(
                user.getLoginId(),
                user.getPassword(),
                user.getUserName()
        );
    }
}
