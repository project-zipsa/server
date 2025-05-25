package navi4.zipsa.infrastructure.api.clova.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseContract.domain.PropertyTitleExtractionTemplate;
import navi4.zipsa.common.dto.SuccessResponse;
import navi4.zipsa.infrastructure.api.clova.application.ClovaOCRService;
import navi4.zipsa.command.JeonseContract.domain.LeaseContractAnalysisTemplate;
import navi4.zipsa.infrastructure.api.gpt.application.GptApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/zipsa/clova")
public class ClovaController {

    private final ClovaOCRService clovaOCRService;
    private final GptApiService gptApiService;
    private final ObjectMapper objectMapper;

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
        Object parsedJson;
        try {
            parsedJson = objectMapper.readValue(contractAnalysisResponse, Object.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("GPT 분석 응답 파싱 실패", e);
        }

        // 4. 결과 저장
        // contractService.extractContractRiskScore(userId, contractAnalysisResponse);
        // JSON 형태의 데이터만 저장 -> 나머지 필드 버리기
        clovaOCRService.updateJeonseContractJson(userId, contractAnalysisResponse);

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
        StringBuilder totalText = new StringBuilder();
        for (MultipartFile landTitle : landTitles) {
            String extractedText = clovaOCRService.extractTextFromFile(landTitle);
            totalText.append(clovaOCRService.extractTextOnly(extractedText));
        }

        String contractAnalysisResponse = gptApiService.chat(
                String.valueOf(totalText)
                        + PropertyTitleExtractionTemplate.REQUEST_MESSAGE
                        + PropertyTitleExtractionTemplate.TEMPLATE).block();

        clovaOCRService.updatePropertyTitleJson(userId, contractAnalysisResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success("등기부등본 추출 및 저장 성공"));
    }

}
