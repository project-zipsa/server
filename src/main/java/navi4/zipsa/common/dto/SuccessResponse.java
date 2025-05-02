package navi4.zipsa.common.dto;

public record SuccessResponse<T>(
        String message,
        T data
) {
    public static <T> SuccessResponse<T> success(String message, T data) {
        return new SuccessResponse<>(message, data);
    }

    public static <T> SuccessResponse<T> success(String message) {
        return new SuccessResponse<>(message, null);
    }
}
