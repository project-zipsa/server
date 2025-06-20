package navi4.zipsa.command.JeonseContract.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseContract.domain.ContractResult;
import navi4.zipsa.command.JeonseContract.domain.ContractResultRepository;
import navi4.zipsa.command.JeonseContract.domain.LeaseContractAnalysisTemplate;
import navi4.zipsa.command.JeonseContract.domain.TotalContractAnalysisTemplate;
import navi4.zipsa.common.exception.ExceptionMessages;
import navi4.zipsa.infrastructure.api.gpt.application.GptApiService;
import navi4.zipsa.infrastructure.api.odg.dto.TotalBrInfoRequest;
import navi4.zipsa.infrastructure.exception.InfraErrorMessages;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractResultRepository contractResultRepository;
    private final ObjectMapper objectMapper;
    private final GptApiService gptApiService;

    private static final String NOT_FOUND_CONTRACT_RESULT = ExceptionMessages.ERROR_PREFIX + "계약서를 불러올 수 없습니다.";
    private static final String NOT_FOUND_JEONSE_CONTRACT_JSON = ExceptionMessages.ERROR_PREFIX + "[Error]: 저장된 전세계약 정보가 없습니다.";
    private static final String NOT_FOUND_PROPERTY_TITLE_JSON = ExceptionMessages.ERROR_PREFIX + "[Error]: 저장된 등기부등본 정보가 없습니다.";
    private static final String FAIL_CONVERSION_BUILDING_REGISTER_TO_JSON = ExceptionMessages.ERROR_PREFIX + "건축물대장 JSON을 String으로 반환, 저장하는 데 실패했습니다";

    public ContractResult getContractResultsByContractId(Long contractId) {
        return contractResultRepository.findContractResultById(contractId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

    public ContractResult getContractResultsByUserId(Long userId) {
        return contractResultRepository.findContractResultsByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));
    }

    public void updateLeaseContractRawText(Long userId, String leaseContractRawText){
        contractResultRepository.updateRawLeaseContractText(userId, leaseContractRawText);
    }

    public Object analyzeLeaseContract(Long userId) {
        try{
            ContractResult contractResult = getContractResultsByUserId(userId);
            String leaseContractRawText = contractResult.getRawLeaseContractText();

            String contractAnalysisResponse = gptApiService.chat(
                    leaseContractRawText
                            + LeaseContractAnalysisTemplate.REQUEST_DETAIL
                            + LeaseContractAnalysisTemplate.RESPONSE_CONDITION
                            + LeaseContractAnalysisTemplate.ANALYSIS_TEMPLATE).block();

            updateJeonseContractJson(userId, contractAnalysisResponse);
            return objectMapper.readValue(contractAnalysisResponse, Object.class);
        } catch (JsonProcessingException e){
            log.error("gpt 전세계약서 분석 실패");
            throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + InfraErrorMessages.JEONSE_CONTRACT_ANALYSIS_ERROR);
        }
    }

    private void updateJeonseContractJson(Long userId, String text){
        if (!contractResultRepository.existsContractResultByUserId(userId)){
            throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + InfraErrorMessages.NOT_EXIST_USER_CONTRACT_RESULT);
        }
        try{
            JsonNode rootNode = objectMapper.readTree(text);
            JsonNode dataNode = rootNode.path("data");
            if (dataNode.isMissingNode()){
                log.error("전세계약서의 data 필드 존재하지 않음");
                throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + InfraErrorMessages.JEONSE_CONTRACT_ANALYSIS_ERROR);
            }
            contractResultRepository.updateJeonseContractJson(userId, dataNode.toPrettyString());
        } catch (Exception e){
            log.error("전세계약서 데이터 JSON 파싱 에러 등 에러 발생" + e.getMessage() + e);
            throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + InfraErrorMessages.JEONSE_CONTRACT_ANALYSIS_ERROR);
        }
    }

    // TODO: 동기 - 비동기 처리 충돌 해결
    public Mono<String> analyzeTotalRisk(Long userId, Mono<TotalBrInfoRequest> brInfoMono) {
        ContractResult contractResult = contractResultRepository.findContractResultsByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CONTRACT_RESULT));

        String jeonseContractJson = Optional.ofNullable(contractResult.getJeonseContractJson())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_JEONSE_CONTRACT_JSON));

        String propertyTitleJson = Optional.ofNullable(contractResult.getPropertyTitleJson())
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PROPERTY_TITLE_JSON));

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
                return Mono.error(new IllegalArgumentException(FAIL_CONVERSION_BUILDING_REGISTER_TO_JSON + "\n" + e.getMessage() + e));
            }
        });
    }
}