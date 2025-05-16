package navi4.zipsa.infrastructure.api.clova.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import navi4.zipsa.command.contract.application.ContractService;
import navi4.zipsa.common.dto.SuccessResponse;
import navi4.zipsa.infrastructure.api.clova.application.ClovaOCRService;
import navi4.zipsa.infrastructure.api.gpt.application.ContractAnalysisTemplate;
import navi4.zipsa.infrastructure.api.gpt.application.GptApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/zipsa/clova")
public class ClovaController {

    private final ClovaOCRService clovaOCRService;
    private final GptApiService gptApiService;
    private final ContractService contractService;

    // 전세계약서 업로드
    // TODO: pdf 업로드도 잘 되는지 확인
    @PostMapping("/upload-lease-contract")
    public ResponseEntity<SuccessResponse<Object>> uploadLeaseContractFile(
            @RequestParam Long userId,
            @RequestParam MultipartFile leaseContractFile) {

        // 1. 파일 -> 텍스트 추출
        String clovaResponseJson = clovaOCRService.extractTextFromImageFile(leaseContractFile);
        String text = clovaOCRService.extractTextOnly(clovaResponseJson);

        // 2. gpt 응답 받기
        String contractAnalysisResponse = gptApiService.chat(text + ContractAnalysisTemplate.GPT_REQUEST_MESSAGE + ContractAnalysisTemplate.CONTRACT_ANALYSIS_TEMPLATE).block();

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

    // TODO: 등기부등본 파일 받기


    // 파일 업로드 테스트 api
    // TODO: 추후 삭제
    @PostMapping("/upload-file-test")
    public ResponseEntity<SuccessResponse<String>> uploadFileTest(@RequestParam MultipartFile file){
        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String fileInfo = String.format("파일 이름: %s, 크기: %d bytes",
                file.getOriginalFilename(), file.getSize());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success(fileInfo));
    }
}
