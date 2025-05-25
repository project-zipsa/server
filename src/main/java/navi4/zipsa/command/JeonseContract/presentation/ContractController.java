package navi4.zipsa.command.JeonseContract.presentation;

import lombok.AllArgsConstructor;
import navi4.zipsa.command.JeonseContract.application.ContractService;
import navi4.zipsa.command.JeonseContract.domain.ContractResult;
import navi4.zipsa.common.dto.SuccessResponse;
import navi4.zipsa.infrastructure.api.odg.application.OdgService;
import navi4.zipsa.infrastructure.api.odg.dto.OdgDefaultRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/zipsa/contract")
public class ContractController {

    private final ContractService contractService;
    private final OdgService odgService;

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

    @PostMapping("/total-contract-analysis")
    public Mono<String> analyzeContract(
            @RequestParam Long userId,
            @ModelAttribute OdgDefaultRequest odgDefaultRequest) {
        return contractService.analyzeTotalRisk(userId, odgService.getTotalBrInfoRequest(odgDefaultRequest));
    }

}
