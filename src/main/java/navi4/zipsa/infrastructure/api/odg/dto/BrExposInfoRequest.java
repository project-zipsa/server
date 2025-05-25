package navi4.zipsa.infrastructure.api.odg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BrExposInfoRequest(
        @JsonProperty("동") String dongNm, // 동
        @JsonProperty("호") String hoNm, // 호
        @JsonProperty("층수") String flrNo
) {
}
