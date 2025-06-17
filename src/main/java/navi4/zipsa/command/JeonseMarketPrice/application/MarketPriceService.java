package navi4.zipsa.command.JeonseMarketPrice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseMarketPrice.dto.*;
import navi4.zipsa.common.exception.ExceptionMessages;
import navi4.zipsa.common.utils.CsvReader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketPriceService {

    private final static String HOUSING_DATA_PATH = "data/housing.csv";
    private final static String FILE_NOT_FOUNT_ERROR = ExceptionMessages.ERROR_PREFIX + "해당 경로에 파일이 존재하지 않습니다.";
    private final static String SIMILAR_DATA_NOT_FOUND_MESSAGE = "입력하신 조건과의 유사 조건 데이터가 존재하지 않습니다.";
    private final static double DEFAULT_HOUSE_PRICE = 0;
    private static final int PERCENT_BASE = 100;

    // TODO: 하드코딩된 값 수정
    public Object calculateMarketPrice(MarketPriceRequest userHousingData) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(HOUSING_DATA_PATH);
        if (inputStream == null) {
            throw new FileNotFoundException(FILE_NOT_FOUNT_ERROR);
        }
        List<String[]> housingData = CsvReader.read(inputStream);

        int totalPrice = 0;
        int sameDataCnt = 0;
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
                    DEFAULT_HOUSE_PRICE
            );
            if (isSameCondition(userHousingData, houseData)){
                totalPrice += Integer.parseInt(datum[4]);
                sameDataCnt ++;
            }
            if (isSimilarCondition(userHousingData, houseData, datum[3])){
                totalRecentDataPrice += Integer.parseInt(datum[4]);
                recentDataCnt ++;
            }
        }

        if (sameDataCnt == 0 || recentDataCnt == 0){
            log.error("요청 주소: {}", userHousingData.address());
            log.error("요청 층수: {}", userHousingData.floor());
            log.error("sameData: {}", sameDataCnt);
            log.error("recentData: {}", recentDataCnt);
            return SIMILAR_DATA_NOT_FOUND_MESSAGE;
        }

        double averageHousePrice = (double) totalPrice / sameDataCnt;
        double userPrice = userHousingData.price();
        double differencePercent = ((userPrice - averageHousePrice) / averageHousePrice) * PERCENT_BASE;
        double averageRecentHousePrice = (double) totalRecentDataPrice / recentDataCnt;
        double jeonseRatio = (userPrice / averageRecentHousePrice) * PERCENT_BASE;
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
                        && houseData.contractType().equals(ContractType.SALE.getDescription())
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
