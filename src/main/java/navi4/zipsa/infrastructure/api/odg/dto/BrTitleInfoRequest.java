package navi4.zipsa.infrastructure.api.odg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BrTitleInfoRequest(
        @JsonProperty("주소") String platPlc,
        @JsonProperty("번") String bun,
        @JsonProperty("지") String ji,
        @JsonProperty("도로명주소") String newPlatPlc,
        @JsonProperty("건물명") String bldNm,
        @JsonProperty("건물용도") String mainPurpsCdNm,
        @JsonProperty("기타용도") String etcPurps,
        @JsonProperty("전용면적") String archArea,
        @JsonProperty("연면적") String totArea,
        @JsonProperty("지상층수") String grndFlrCnt,
        @JsonProperty("지하층수") String ugrndFlrCnt,
        @JsonProperty("건물구조") String strctCdNm,
        @JsonProperty("기타구조") String etcStrct,
        @JsonProperty("사용ㄱ승인일") String useAprDay
) {
}

