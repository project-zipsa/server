package navi4.zipsa.infrastructure.api.odg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BrJijiguInfoRequest(
        @JsonProperty("용도지역코드") String jijiguGbCdNm,
        @JsonProperty("일반주거지역") String jijiguCdNm
) {
}
