package navi4.zipsa.command.JeonseContract.dto;

import java.math.BigInteger;
import java.util.Date;

public record ContractPriceComparisonRequest(
        BigInteger myContractPrice,
        String address,
        float houseAreaM2,
        Date contractDate
) {
}
