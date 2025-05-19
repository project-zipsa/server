package navi4.zipsa.command.JeonseMarketPrice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseMarketPrice.dto.*;
import navi4.zipsa.common.utils.CsvReader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketPriceService {

    private static final String housingDataFilePath = "src/main/resources/data/housing.csv";

    public MarketPriceResponse calculateMarketPrice(MarketPriceRequest userHousingData) throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/housing.csv");

        if (inputStream == null) {
            throw new FileNotFoundException("housing.csv not found in classpath");
        }

        List<String[]> housingData = CsvReader.read(inputStream);

        //List<String[]> housingData = CsvReader.read(housingDataFilePath);

        int totalPrice = 0;
        int cnt = 0;
        int totalRecentDataPrice = 0;
        int recentDataCnt = 0;
        for (String[] datum : housingData) {
            MarketPriceRequest houseData = new MarketPriceRequest(
                    datum[1],
                    Double.parseDouble(datum[2]),
                    Integer.parseInt(datum[5]),
                    Integer.parseInt(datum[6]),
                    datum[7],
                    datum[8],
                    0
            );
            if (isSameCondition(userHousingData, houseData)){
                totalPrice += Integer.parseInt(datum[4]);
                cnt ++;
            }


            if (isSimilarCondition(userHousingData, houseData, datum[3])){
                totalRecentDataPrice += Integer.parseInt(datum[4]);
                recentDataCnt ++;
            }

        }

        double averageHousePrice = (double) totalPrice / cnt;
        double userPrice = userHousingData.price();
        double differencePercent = ((userPrice - averageHousePrice) / averageHousePrice) * 100;

        double averageRecentHousePrice = (double) totalRecentDataPrice / recentDataCnt;
        double jeonseRatio = (userPrice / averageRecentHousePrice) * 100;
        jeonseRatio = Math.round(jeonseRatio * 10) / 10.0;
        return new MarketPriceResponse(averageHousePrice, userPrice, differencePercent, jeonseRatio);
    }

    private boolean isSameCondition(MarketPriceRequest userHousingData, MarketPriceRequest houseData){
        return (
                userHousingData.address().equals(houseData.address())
                        && AreaRangeType.areInSameRange(userHousingData.area(), houseData.area())
                        && FloorRangeType.areInSameRange(userHousingData.floor(), houseData.floor())
                        && BuiltYearRangeType.areInSameRange(userHousingData.builtYear(), houseData.builtYear())
                        && userHousingData.housingType().equals(houseData.housingType())
                        && userHousingData.contractType().equals(houseData.contractType())
                );
    }

    private boolean isSimilarCondition(MarketPriceRequest userHousingData, MarketPriceRequest houseData, String yearMonth){
        return (
                userHousingData.address().equals(houseData.address())
                        && AreaRangeType.areInSameRange(userHousingData.area(), houseData.area())
                        && isInRecentDateRange(yearMonth)
                );
    }

    public static boolean isInRecentDateRange(String inputYearMonth) {
        YearMonth input = YearMonth.parse(inputYearMonth, java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));

        YearMonth start = YearMonth.of(2024, 5);
        YearMonth end = YearMonth.of(2025, 5);

        return !input.isBefore(start) && !input.isAfter(end);
    }

}
