package navi4.zipsa.command.JeonseMarketPrice.dto;

public record MarketPriceRequest(
        String address,
        double area,
        Integer floor,
        Integer builtYear,
        String housingType,
        String contractType,
        double price
) {
}
