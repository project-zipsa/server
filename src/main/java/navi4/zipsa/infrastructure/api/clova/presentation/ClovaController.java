package navi4.zipsa.infrastructure.api.clova.presentation;

import lombok.AllArgsConstructor;
import navi4.zipsa.common.dto.SuccessResponse;
import navi4.zipsa.infrastructure.api.clova.application.ClovaOCRService;
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

    // 전세계약서 업로드
    // TODO: pdf 업로드도 잘 되는지 확인
    @PostMapping("/upload-lease-contract")
    public ResponseEntity<SuccessResponse<String>> uploadLeaseContractFile(@RequestParam Long userId, @RequestParam MultipartFile leaseContractFile) {

        String clovaResponseJson = clovaOCRService.extractTextFromImageFile(leaseContractFile);
        String text = clovaOCRService.extractTextOnly(clovaResponseJson);
        clovaOCRService.updateJeonseContractText(userId, text);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success("전세계약서 추출 및 저장 성공"));

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


