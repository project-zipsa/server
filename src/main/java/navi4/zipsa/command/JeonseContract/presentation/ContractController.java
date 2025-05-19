package navi4.zipsa.command.JeonseContract.presentation;

import lombok.AllArgsConstructor;
import navi4.zipsa.command.JeonseContract.application.ContractService;
import navi4.zipsa.command.JeonseContract.domain.ContractResult;
import navi4.zipsa.command.JeonseContract.dto.ContractPriceComparisonRequest;
import navi4.zipsa.common.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/zipsa/contract")
public class ContractController {

    private final ContractService contractService;

    @GetMapping("/get-by-contract-id")
    public ResponseEntity<SuccessResponse<ContractResult>> getContractsByContractId(@RequestParam Long contractId) {
        ContractResult result = contractService.getContractResultsByContractId(contractId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success(result));
    }

    @GetMapping("/get-by-user-id")
    public ResponseEntity<SuccessResponse<ContractResult>> getContractsByUserId(@RequestParam Long userId) {
        ContractResult result = contractService.getContractResultsByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success(result));
    }

    @PostMapping("/get-contract-price-comparison-result")
    public ResponseEntity<SuccessResponse<ContractResult>> getContractPriceComparisonResult(@RequestBody ContractPriceComparisonRequest request) {
        //contractService.
        return null;
    }

}
