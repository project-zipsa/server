package navi4.zipsa.command.JeonseMarketPrice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContractType {
    JEONSE("전세"),
    SALE("매매");

    private final String description;

    ContractType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static ContractType from(String contractType) {
        for (ContractType type : values()) {
            if (type.description.equals(contractType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 거래 유형입니다: " + contractType);
    }
}
