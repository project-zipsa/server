package navi4.zipsa.command.JeonseContract.dto;

import java.math.BigInteger;

public record ContractPriceComparisonResponse(
        BigInteger myContractPrice,
        BigInteger comparableAveragePrice,
        int differenceRate
) {
}
