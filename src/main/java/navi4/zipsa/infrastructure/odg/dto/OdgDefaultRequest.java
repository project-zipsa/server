package navi4.zipsa.infrastructure.odg.dto;

import java.util.Map;

public record OdgDefaultRequest(
        String sigunguCd,
        String bjdongCd,
        Map<String, Object> metadata
) {
}
