package navi4.zipsa.command.JeonseMarketPrice.presentation;

import lombok.AllArgsConstructor;
import navi4.zipsa.command.JeonseMarketPrice.application.MarketPriceService;
import navi4.zipsa.command.JeonseMarketPrice.dto.MarketPriceRequest;
import navi4.zipsa.command.JeonseMarketPrice.dto.MarketPriceResponse;
import navi4.zipsa.common.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/market-price")
public class MarketPriceController {

    private final MarketPriceService marketPriceService;

    @PostMapping("/analysis")
    public ResponseEntity<SuccessResponse<MarketPriceResponse>> getMarketPriceInfo(@RequestBody MarketPriceRequest userHousingData) throws IOException {
        MarketPriceResponse response = marketPriceService.calculateMarketPrice(userHousingData);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success("시세 정보 추출 성공", response));
    }

}
