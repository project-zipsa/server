package navi4.zipsa.command.JeonseMarketPrice.dto;

import java.util.Arrays;

public enum BuiltYearRangeType {
    YEAR_1900_1904(1900, 1904),
    YEAR_1905_1909(1905, 1909),
    YEAR_1910_1914(1910, 1914),
    YEAR_1915_1919(1915, 1919),
    YEAR_1920_1924(1920, 1924),
    YEAR_1925_1929(1925, 1929),
    YEAR_1930_1934(1930, 1934),
    YEAR_1935_1939(1935, 1939),
    YEAR_1940_1944(1940, 1944),
    YEAR_1945_1949(1945, 1949),
    YEAR_1950_1954(1950, 1954),
    YEAR_1955_1959(1955, 1959),
    YEAR_1960_1964(1960, 1964),
    YEAR_1965_1969(1965, 1969),
    YEAR_1970_1974(1970, 1974),
    YEAR_1975_1979(1975, 1979),
    YEAR_1980_1984(1980, 1984),
    YEAR_1985_1989(1985, 1989),
    YEAR_1990_1994(1990, 1994),
    YEAR_1995_1999(1995, 1999),
    YEAR_2000_2004(2000, 2004),
    YEAR_2005_2009(2005, 2009),
    YEAR_2010_2014(2010, 2014),
    YEAR_2015_2019(2015, 2019),
    YEAR_2020_2025(2020, 2025);

    private final int min;
    private final int max;

    BuiltYearRangeType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(int year) {
        return year >= min && year <= max;
    }

    public static BuiltYearRangeType from(int year) {
        return Arrays.stream(values())
                .filter(r -> r.contains(year))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 건축년도 타입입니다: " + year));
    }

    public static boolean areInSameRange(int year1, int year2) {
        BuiltYearRangeType range1 = from(year1);
        BuiltYearRangeType range2 = from(year2);
        return range1 == range2;
    }
}
