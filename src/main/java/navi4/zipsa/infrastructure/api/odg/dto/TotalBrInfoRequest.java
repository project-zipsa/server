package navi4.zipsa.infrastructure.api.odg.dto;

public record TotalBrInfoRequest(
        BrTitleInfoRequest titleInfo,
        BrExposInfoRequest exposInfo,
        BrJijiguInfoRequest jijiguInfo
) {
}
