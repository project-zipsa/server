package navi4.zipsa.command.user.dto;

public record LoginRequest(
        String loginId,
        String password
) {
}
