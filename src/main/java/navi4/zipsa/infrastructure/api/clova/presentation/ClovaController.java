package navi4.zipsa.infrastructure.api.clova.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseContract.application.ContractService;
import navi4.zipsa.command.JeonseContract.domain.LeaseContractAnalysisTemplate;
import navi4.zipsa.command.JeonseContract.domain.PropertyTitleExtractionTemplate;
import navi4.zipsa.common.dto.SuccessResponse;
import navi4.zipsa.infrastructure.api.clova.application.ClovaOCRService;
import navi4.zipsa.infrastructure.api.gpt.application.GptApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/clova")
public class ClovaController {

    private final ClovaOCRService clovaOCRService;
    private final GptApiService gptApiService;
    private final ObjectMapper objectMapper;

    private static final String SUCCESS_PREFIX = "[SUCCESS]: ";
    private static final String SUCCESS_LEASE_CONTRACTS_EXTRACTION = SUCCESS_PREFIX + "전세계약서 추출 및 저장 성공";
    private static final String SUCCESS_PROPERTY_TITLE_EXTRACTION = SUCCESS_PREFIX + "등기부등본 추출 및 저장 성공";
    private final ContractService contractService;

    @PostMapping("/lease-contracts")
    public ResponseEntity<SuccessResponse<Object>> uploadLeaseContractFile (
            @RequestParam Long userId,
            @RequestParam MultipartFile[] leaseContractFiles
    ) throws JsonProcessingException {
        StringBuilder totalText = new StringBuilder();
        for (MultipartFile leaseContractFile : leaseContractFiles) {
            String extractedText = clovaOCRService.extractTextFromFile(leaseContractFile);
            totalText.append(clovaOCRService.extractTextOnly(extractedText));
        }
        contractService.updateLeaseContractRawText(userId, String.valueOf(totalText));
        String contractAnalysisResponse = gptApiService.chat(
                totalText
                            + LeaseContractAnalysisTemplate.REQUEST_DETAIL
                            + LeaseContractAnalysisTemplate.RESPONSE_CONDITION
                            + LeaseContractAnalysisTemplate.ANALYSIS_TEMPLATE).block();

            Object parsedJson = objectMapper.readValue(contractAnalysisResponse, Object.class);
            clovaOCRService.updateJeonseContractJson(userId, contractAnalysisResponse);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(SuccessResponse.success(SUCCESS_LEASE_CONTRACTS_EXTRACTION, parsedJson));
    }

    @GetMapping("/lease-contracts")
    public ResponseEntity<SuccessResponse<Object>> getLeaseContracts (@RequestParam Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success("전세계약서 필드 추출 성공", contractService.analyzeLeaseContract(userId)));
    }

    @PostMapping("/land-titles")
    public ResponseEntity<SuccessResponse<Object>> uploadLandTitleFile(
            @RequestParam Long userId,
            @RequestParam MultipartFile[] landTitles
    ) throws JsonProcessingException {
        StringBuilder totalText = new StringBuilder();
        for (MultipartFile landTitle : landTitles) {
            String extractedText = clovaOCRService.extractTextFromFile(landTitle);
            totalText.append(clovaOCRService.extractTextOnly(extractedText));
        }

        String contractAnalysisResponse = gptApiService.chat(
                totalText
                        + PropertyTitleExtractionTemplate.REQUEST_MESSAGE
                        + PropertyTitleExtractionTemplate.TEMPLATE).block();

        Object parsedJson = objectMapper.readValue(contractAnalysisResponse, Object.class);
        clovaOCRService.updatePropertyTitleJson(userId, contractAnalysisResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success(SUCCESS_PROPERTY_TITLE_EXTRACTION, parsedJson));
    }
}