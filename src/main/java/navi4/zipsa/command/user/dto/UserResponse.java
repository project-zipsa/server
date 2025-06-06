package navi4.zipsa.command.user.dto;

import navi4.zipsa.command.user.domain.User;

public record UserResponse(
        Long userId,
        String loginId,
        String password,
        String username
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getLoginId(), user.getPassword(), user.getUserName());
    }
}
