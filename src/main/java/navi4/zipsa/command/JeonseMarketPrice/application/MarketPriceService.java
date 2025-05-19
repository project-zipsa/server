package navi4.zipsa.command.JeonseMarketPrice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseMarketPrice.dto.*;
import navi4.zipsa.common.utils.CsvReader;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketPriceService {

    private static final String housingDataFilePath = "src/main/resources/data/housing.csv";

    public MarketPriceResponse calculateMarketPrice(MarketPriceRequest userHousingData){
        List<String[]> housingData = CsvReader.read(housingDataFilePath);

        int totalPrice = 0;
        int cnt = 0;
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
            if(isSameCondition(userHousingData, houseData)){
                totalPrice += Integer.parseInt(datum[4]);
                cnt ++;
            }
        }
        double averageHousePrice = (double) totalPrice / cnt;
        double userPrice = userHousingData.price();
        double differencePercent = ((averageHousePrice - userPrice) / averageHousePrice) * 100;
        return new MarketPriceResponse(averageHousePrice, userPrice, differencePercent);
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

}
