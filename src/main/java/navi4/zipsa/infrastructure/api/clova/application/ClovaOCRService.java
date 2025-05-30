package navi4.zipsa.infrastructure.api.clova.application;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.command.JeonseContract.domain.ContractResultRepository;
import navi4.zipsa.common.exception.ExceptionMessages;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaOCRService {

    private final ContractResultRepository contractResultRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${clova.invokeUrl}")
    private String clovaInvokeUrl;

    @Value("${clova.secretKey}")
    private String clovaSecretKey;

    // TODO: 메시지 상수화
    public String extractTextFromFile(MultipartFile rawFile) {
        try {
            List<ClovaOCRImageBody> files = new ArrayList<>();
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

            ByteArrayResource fileAsResource = new ByteArrayResource(rawFile.getBytes()) {
                @Override
                public String getFilename() {
                    return rawFile.getOriginalFilename();
                }
            };
            formData.add("file", fileAsResource);

            String extension = rawFile.getOriginalFilename()
                    .substring(rawFile.getOriginalFilename().lastIndexOf('.') + 1);

            files.add(new ClovaOCRImageBody(extension, rawFile.getOriginalFilename()));
            ClovaOCRMessageBody messageBody = new ClovaOCRMessageBody(
                    "V2", "1234", System.currentTimeMillis(), "ko", files
            );

            String messageJson = objectMapper.writeValueAsString(messageBody);
            formData.add("message", messageJson);

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

    public void updateJeonseContractJson(Long userId, String text){
        if (!contractResultRepository.existsContractResultByUserId(userId)){
            throw new IllegalArgumentException("해당 유저의 계약 결과가 존재하지 않습니다.");
        }

        try{
            JsonNode rootNode = objectMapper.readTree(text);
            JsonNode dataNode = rootNode.path("data");
            if (dataNode.isMissingNode()){
                log.error("전세계약서의 data 필드 존재하지 않음");
                throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + "전세계약서 분석 오류");
            }
            contractResultRepository.updateJeonseContractJson(userId, dataNode.toPrettyString());
        } catch (JsonProcessingException e){
            log.error("전세계약서 데이터 JSON 파싱 에러" + e.getMessage() + e);
            throw new IllegalArgumentException("전세계약서 분석 오류");
        }
    }

    public void updatePropertyTitleJson(Long userId, String text){
        if (!contractResultRepository.existsContractResultByUserId(userId)){
            throw new IllegalArgumentException("해당 유저의 계약 결과가 존재하지 않습니다.");
        }

        try{
            JsonNode rootNode = objectMapper.readTree(text);
            JsonNode dataNode = rootNode.path("data");

            if (dataNode.isMissingNode()){
                throw new IllegalArgumentException("등기부등본의 data 필드 존재하지 않음");
            }
            contractResultRepository.updatePropertyTitleJson(userId, dataNode.toPrettyString());
        } catch (JsonProcessingException e){
            throw new IllegalArgumentException("등기부등본 데이터 JSON 파싱 에러" + e.getMessage(), e);
        }
    }
}

