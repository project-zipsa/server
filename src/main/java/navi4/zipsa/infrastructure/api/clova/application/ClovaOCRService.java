package navi4.zipsa.infrastructure.api.clova.application;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import navi4.zipsa.command.JeonseContract.domain.ContractResultRepository;
import navi4.zipsa.infrastructure.api.clova.dto.ClovaOCRImageBody;
import navi4.zipsa.infrastructure.api.clova.dto.ClovaOCRMessageBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClovaOCRService {

    private final ContractResultRepository contractResultRepository;

    @Value("${clova.invokeUrl}")
    private String clovaInvokeUrl;

    @Value("${clova.secretKey}")
    private String clovaSecretKey;

    private final WebClient webClient;

    public String extractTextFromFile(MultipartFile rawFile) {
        try {
            List<ClovaOCRImageBody> files = new ArrayList<>();
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

            // 파일 리소스 설정
            ByteArrayResource fileAsResource = new ByteArrayResource(rawFile.getBytes()) {
                @Override
                public String getFilename() {
                    return rawFile.getOriginalFilename();
                }
            };
            formData.add("file", fileAsResource);

            // 확장자 추출
            String extension = rawFile.getOriginalFilename()
                    .substring(rawFile.getOriginalFilename().lastIndexOf('.') + 1);

            // OCR 이미지 바디 생성
            files.add(new ClovaOCRImageBody(extension, rawFile.getOriginalFilename()));

            // 메시지 바디 생성
            ClovaOCRMessageBody messageBody = new ClovaOCRMessageBody(
                    "V2", "1234", System.currentTimeMillis(), "ko", files
            );

            // JSON 메시지 추가
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(messageBody);
            formData.add("message", messageJson);

            // 요청
            return webClient.post()
                    .uri(clovaInvokeUrl)
                    .header("X-OCR-SECRET", clovaSecretKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            throw new IllegalArgumentException("[텍스트 추출 요청 실패]: " + e.getMessage(), e);
        }
    }


    public String extractTextOnly(String clovaResponseJson){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(clovaResponseJson);

            JsonNode fields = root
                    .path("images")
                    .get(0)
                    .path("fields");

            StringBuilder result = new StringBuilder();
            for (JsonNode field : fields) {
                String text = field.path("inferText").asText();
                result.append(text).append(" ");
            }
            return result.toString().trim();

        } catch (JacksonException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void updateJeonseContractText(Long userId, String text){
        if (!contractResultRepository.existsContractResultByUserId(userId)){
            throw new IllegalArgumentException("해당 유저의 계약 결과가 존재하지 않습니다.");
        }
        contractResultRepository.updateJeonseContractText(userId, text);
    }

    public void updatePropertyTitleText(Long userId, String text){
        if (!contractResultRepository.existsContractResultByUserId(userId)){
            throw new IllegalArgumentException("해당 유저의 계약 결과가 존재하지 않습니다.");
        }
        contractResultRepository.updatePropertyTitleText(userId, text);
    }
}

