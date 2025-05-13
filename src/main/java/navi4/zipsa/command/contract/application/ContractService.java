package navi4.zipsa.command.contract.application;

import lombok.RequiredArgsConstructor;
import navi4.zipsa.command.contract.domain.ContractResult;
import navi4.zipsa.command.contract.domain.ContractResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractResultRepository contractResultRepository;

    private static final String NOT_FOUND_CONTRACT_RESULT = "계약서를 불러올 수 없습니다.";

    public ContractResult getContractResultsByContractId(Long contractId) {
        return contractResultRepository.findContractResultById(contractId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

    public List<ContractResult> getContractResultsByUserId(Long userId) {
        return contractResultRepository.findContractResultsByUsersId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

}
