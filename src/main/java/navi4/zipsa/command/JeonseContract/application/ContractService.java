package navi4.zipsa.command.JeonseContract.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseContract.domain.ContractResult;
import navi4.zipsa.command.JeonseContract.domain.ContractResultRepository;
import navi4.zipsa.command.JeonseContract.domain.TotalContractAnalysisTemplate;
import navi4.zipsa.infrastructure.api.gpt.application.GptApiService;
import navi4.zipsa.infrastructure.api.odg.dto.TotalBrInfoRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractResultRepository contractResultRepository;
    private final ObjectMapper objectMapper;
    private final GptApiService gptApiService; // TODO: 서비스 간 의존이 한 방향인지 재확인

    private static final String NOT_FOUND_CONTRACT_RESULT = "계약서를 불러올 수 없습니다.";

    public ContractResult getContractResultsByContractId(Long contractId) {
        return contractResultRepository.findContractResultById(contractId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

    public ContractResult getContractResultsByUserId(Long userId) {
        return contractResultRepository.findContractResultsByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

    // TODO: 동기 - 비동기 처리 충돌 해결
    public Mono<String> analyzeTotalRisk(Long userId, Mono<TotalBrInfoRequest> brInfoMono) {
        ContractResult contractResult = contractResultRepository.findContractResultsByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));

        String jeonseContractJson = contractResult.getJeonseContractJson();
        String propertyTitleJson = contractResult.getPropertyTitleJson();

        return brInfoMono.flatMap(brInfo -> {
            try {
                String buildingRegisterJson = objectMapper.writeValueAsString(brInfo);
                contractResultRepository.updateBuildingRegisterJson(userId, buildingRegisterJson);

                String prompt = "\n[전세계약서 텍스트]:\n" + jeonseContractJson +
                        "\n[등기부등본 텍스트]:\n" + propertyTitleJson +
                        "\n[건축물대장 데이터]:\n" + buildingRegisterJson +
                        TotalContractAnalysisTemplate.REQUEST_MESSAGE +
                        TotalContractAnalysisTemplate.ANALYSIS_DETAIL +
                        TotalContractAnalysisTemplate.PRINT_FORMAT;
                return gptApiService.chat(prompt);
            } catch (JsonProcessingException e) {
                return Mono.error(new IllegalArgumentException("건축물대장 JSON을 String으로 반환저장하는 로직 오류" + e.getMessage() + e));
            }
        });
    }

}