package navi4.zipsa.command.JeonseMarketPrice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HousingType {
    MULTI_FAMILY("다가구"),
    VILLA("다세대"),
    DETACHED("단독"),
    APARTMENT("아파트"),
    ROW_HOUSE("연립"),
    ROW_MULTI("연립다세대"),
    OFFICETEL("오피스텔");

    private final String description;

    HousingType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static HousingType from(String housingType) {
        for (HousingType type : values()) {
            if (type.description.equals(housingType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 주택 유형입니다: " + housingType);
    }

}
