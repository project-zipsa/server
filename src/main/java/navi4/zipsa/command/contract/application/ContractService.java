package navi4.zipsa.command.contract.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import navi4.zipsa.command.contract.domain.ContractResult;
import navi4.zipsa.command.contract.domain.ContractResultRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractResultRepository contractResultRepository;
    private final ObjectMapper objectMapper;

    private static final String NOT_FOUND_CONTRACT_RESULT = "계약서를 불러올 수 없습니다.";

    public ContractResult getContractResultsByContractId(Long contractId) {
        return contractResultRepository.findContractResultById(contractId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

    public ContractResult getContractResultsByUserId(Long userId) {
        return contractResultRepository.findContractResultsByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

    // jeonseContractRiskScore 업데이트
    public void extractContractRiskScore(Long userId, String contractAnalysisResponse){
        try{
            JsonNode root = objectMapper.readTree(contractAnalysisResponse);
            String score = root.path("전체 위험도 점수").asText().trim();
            contractResultRepository.updateJeonseContractRiskScore(userId, Integer.parseInt(score));
        } catch (Exception e){
            throw new IllegalArgumentException("전세계약서 위험도 점수 추출 오류" + e);
        }
    }

}