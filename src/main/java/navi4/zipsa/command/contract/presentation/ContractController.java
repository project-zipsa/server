package navi4.zipsa.command.contract.presentation;

import lombok.AllArgsConstructor;
import navi4.zipsa.command.contract.application.ContractService;
import navi4.zipsa.command.contract.domain.ContractResult;
import navi4.zipsa.common.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/zipsa/contract")
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public ResponseEntity<SuccessResponse<ContractResult>> getContractsByContractId(@RequestParam Long contractId) {
        ContractResult result = contractService.getContractResultsByContractId(contractId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<ContractResult>>> getContractsByUserId(@RequestParam Long userId) {
        List<ContractResult> results = contractService.getContractResultsByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success(results));
    }

}
