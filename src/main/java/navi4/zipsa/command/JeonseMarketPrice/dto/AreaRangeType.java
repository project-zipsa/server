package navi4.zipsa.command.JeonseMarketPrice.dto;

import java.util.Arrays;

public enum AreaRangeType {
    BELOW_33(0, 33),
    BELOW_66(33, 66),
    BELOW_99(66, 99),
    BELOW_132(99, 132),
    BELOW_165(132, 165),
    BELOW_198(165, 198),
    BELOW_300(198, 300),
    ABOVE_300(300, Integer.MAX_VALUE);

    private final int min;
    private final int max;

    AreaRangeType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(double area) {
        return area >= min && area < max;
    }

    public static AreaRangeType from(double area) {
        return Arrays.stream(values())
                .filter(r -> r.contains(area))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 면적입니다: " + area));
    }

    public static boolean areInSameRange(double area1, double area2) {
        AreaRangeType range1 = from(area1);
        AreaRangeType range2 = from(area2);
        return range1 == range2;
    }
}
