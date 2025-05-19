package navi4.zipsa.command.JeonseMarketPrice.dto;

import java.util.Arrays;

public enum FloorRangeType {
    BASEMENT(-2, -1),
    FLOOR_1_5(1, 5),
    FLOOR_6_10(6, 10),
    FLOOR_11_15(11, 15),
    FLOOR_16_20(16, 20),
    FLOOR_21_25(21, 25),
    FLOOR_26_30(26, 30),
    FLOOR_31_35(31, 35),
    FLOOR_36_40(36, 40),
    FLOOR_41_45(41, 45),
    FLOOR_46_50(46, 50),
    FLOOR_51_55(51, 55),
    FLOOR_56_60(56, 60),
    FLOOR_61_65(61, 65),
    FLOOR_66_67(66, 70);

    private final int min;
    private final int max;

    FloorRangeType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(int floor) {
        return floor >= min && floor < max;
    }

    public static FloorRangeType from(int floor) {
        return Arrays.stream(values())
                .filter(r -> r.contains(floor))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 층수입니다: " + floor));
    }

    public static boolean areInSameRange(int floor1, int floor2) {
        FloorRangeType range1 = from(floor1);
        FloorRangeType range2 = from(floor2);
        return range1 == range2;
    }
}
