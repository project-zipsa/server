package navi4.zipsa.command.JeonseContract.application;

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
    private final GptApiService gptApiService;

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


    public Mono<String> analyzeTotalRisk(Long userId, Mono<TotalBrInfoRequest> brInfoMono) {
        ContractResult contractResult = contractResultRepository.findContractResultsByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));

        String jeonseContractText = contractResult.getJeonseContractText();
        String propertyTitleText = contractResult.getPropertyTitleText();
        //brInfo

        // 템플릿 추가 + gpt 쓰기
//            return gptApiService.chat(TotalContractAnalysisTemplate.TEMPLATE +
//                    ("\n[전세계약서 텍스트]:\n") + jeonseContractText +
//                    ("\n[등기부등본 텍스트]:\n") + propertyTitleText +
//                    ("\n[건축물대장 데이터]:\n") + brInfo.block()
//            );
        return brInfoMono.flatMap(brInfo -> {
            String prompt = TotalContractAnalysisTemplate.TEMPLATE +
                    "\n[전세계약서 텍스트]:\n" + jeonseContractText +
                    "\n[등기부등본 텍스트]:\n" + propertyTitleText +
                    "\n[건축물대장 데이터]:\n" + brInfo.toString(); // 필요 시 JSON 직렬화
            return gptApiService.chat(prompt);
        });
    }

}