package navi4.zipsa.infrastructure.api.odg.dto;

public record BrTitleInfoRequest(
        String platPlc, // 주소
        String bun,
        String ji,
        String newPlatPlc,
        String bldNm, // 빌딩이름
        String mainPurpsCdNm, // 공동주택
        String etcPurps, // 아파트
        String archArea, // 면적?
        String totArea,
        String grndFlrCnt, // 지상층수
        String ugrndFlrCnt, // 지하층수
        String strctCdNm, // 철근 콘크리트 구조
        String etcStrct,
        String useAprDay // 사용 승인일
) {
}

