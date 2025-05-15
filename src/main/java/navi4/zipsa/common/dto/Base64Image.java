package navi4.zipsa.common.dto;

public record Base64Image(
        String base64Data,
        String contentType,
        String contentName
) {}
