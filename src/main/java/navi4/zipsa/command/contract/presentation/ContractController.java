package navi4.zipsa.command.contract.presentation;

import lombok.AllArgsConstructor;
import navi4.zipsa.command.contract.application.ContractService;
import navi4.zipsa.command.contract.domain.ContractResult;
import navi4.zipsa.common.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
