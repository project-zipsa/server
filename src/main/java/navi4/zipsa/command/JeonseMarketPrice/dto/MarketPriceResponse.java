package navi4.zipsa.command.JeonseMarketPrice.dto;

public record MarketPriceResponse(
        double averagePrice,
        double userPrice,
        double differentPercent,
        double jeonseRatio
) {
}
