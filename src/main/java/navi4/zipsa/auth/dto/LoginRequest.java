package navi4.zipsa.auth.dto;

public record LoginRequest(
        String loginId,
        String password
) {
}
