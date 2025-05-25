package navi4.zipsa.infrastructure.api.odg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TotalBrInfoRequest(
        @JsonProperty("포괄정보") BrTitleInfoRequest titleInfo,
        @JsonProperty("세부정보") BrExposInfoRequest exposInfo,
        @JsonProperty("지역지구정보") BrJijiguInfoRequest jijiguInfo
) {
}
