package navi4.zipsa.infrastructure.api.clova.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseContract.application.ContractService;
import navi4.zipsa.common.dto.SuccessResponse;
import navi4.zipsa.infrastructure.api.clova.application.ClovaOCRService;
import navi4.zipsa.command.JeonseContract.domain.LeaseContractAnalysisTemplate;
import navi4.zipsa.infrastructure.api.gpt.application.GptApiService;
import navi4.zipsa.infrastructure.api.odg.application.OdgService;
import navi4.zipsa.infrastructure.api.odg.dto.OdgDefaultRequest;
import navi4.zipsa.infrastructure.api.odg.dto.TotalBrInfoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/zipsa/clova")
public class ClovaController {

    private final ClovaOCRService clovaOCRService;
    private final GptApiService gptApiService;
    private final ContractService contractService;
    private final OdgService odgService;

    // 전세계약서 업로드
    @PostMapping("/upload-lease-contract")
    public ResponseEntity<SuccessResponse<Object>> uploadLeaseContractFile(
            @RequestParam Long userId,
            @RequestParam MultipartFile leaseContractFile) {

        // 1. 파일 -> 텍스트 추출
        String extractedText = clovaOCRService.extractTextFromFile(leaseContractFile);
        String text = clovaOCRService.extractTextOnly(extractedText);

        // 2. gpt 응답 받기
        String contractAnalysisResponse = gptApiService.chat(
                text
                        + LeaseContractAnalysisTemplate.REQUEST_DETAIL
                        + LeaseContractAnalysisTemplate.RESPONSE_CONDITION
                        + LeaseContractAnalysisTemplate.ANALYSIS_TEMPLATE).block();

        // 3. 응답에서 필요 정보 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Object parsedJson;
        try {
            parsedJson = objectMapper.readValue(contractAnalysisResponse, Object.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("GPT 분석 응답 파싱 실패", e);
        }

        // 4. 결과 저장
        contractService.extractContractRiskScore(userId, contractAnalysisResponse);
        clovaOCRService.updateJeonseContractText(userId, text);

        // 5. 응답
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success("전세계약서 추출 및 저장 성공", parsedJson));
    }

    // 등기부등본 업로드
    @PostMapping("/upload-land-title-file")
    public ResponseEntity<SuccessResponse<Object>> uploadLandTitleFile(
            @RequestParam Long userId,
            @RequestParam MultipartFile[] landTitles
    ){
        // 등기부등본에서 텍스트 추출 + 저장

        StringBuilder totalText = new StringBuilder();
        for (MultipartFile landTitle : landTitles) {
            String extractedText = clovaOCRService.extractTextFromFile(landTitle);
            totalText.append(clovaOCRService.extractTextOnly(extractedText));
        }

        clovaOCRService.updatePropertyTitleText(userId, String.valueOf(totalText));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success("등기부등본 추출 및 저장 성공", String.valueOf(totalText)));
    }

    // TODO: 이거 contractController로 옮기기
    @PostMapping("/analyze-contract")
    public Mono<String> analyzeContract(
            @RequestParam Long userId,
            @ModelAttribute OdgDefaultRequest odgDefaultRequest
    ){
        try{
            // 건축물대장 데이터 추출
            Mono<TotalBrInfoRequest> totalBrInfoResponse = odgService.getTotalBrInfoRequest(odgDefaultRequest);

            // 등기부등본 text VS 건축물대장 text VS 전세계약서 JSON -> 점수 업데이트
            Mono<String> result = contractService.analyzeTotalRisk(userId, totalBrInfoResponse);
            result.subscribe(data -> log.info("🔍 결과: {}", data));
            return result;
        } catch (Exception e){
            throw new IllegalArgumentException("왜 에러: " + e);
        }

        //TODO: 위험도 점수 전송
    }
}
