package navi4.zipsa.infrastructure.api.clova.dto;

import java.util.List;

public record ClovaOCRMessageBody(
        String version,
        String requestId,
        Long timestamp,
        String lang,
        List<ClovaOCRImageBody> images
) {
}
