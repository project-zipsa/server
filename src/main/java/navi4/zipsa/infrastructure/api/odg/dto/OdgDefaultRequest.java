package navi4.zipsa.infrastructure.api.odg.dto;

import java.util.Map;

public record OdgDefaultRequest(
        String sigunguCd,
        String bjdongCd,
        Map<String, Object> metadata
) {
}
