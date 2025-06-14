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
import navi4.zipsa.infrastructure.exception.InfraErrorMessages;
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

    private static final String CLOVA_OCR_VERSION = "V2";
    private static final String CLOVA_OCR_REQUEST_ID = "1234";
    private static final String CLOVA_OCR_LANGUAGE = "ko";

    private final ContractResultRepository contractResultRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${clova.invokeUrl}")
    private String clovaInvokeUrl;

    @Value("${clova.secretKey}")
    private String clovaSecretKey;

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
                    CLOVA_OCR_VERSION, CLOVA_OCR_REQUEST_ID, System.currentTimeMillis(), CLOVA_OCR_LANGUAGE, files
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
            throw new IllegalArgumentException(InfraErrorMessages.CLOVA_TEXT_EXTRACTION_FAILED_PREFIX + e.getMessage(), e);
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

    public void updatePropertyTitleJson(Long userId, String text){
        if (!contractResultRepository.existsContractResultByUserId(userId)){
            throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + InfraErrorMessages.NOT_EXIST_USER_CONTRACT_RESULT);
        }
        try{
            JsonNode rootNode = objectMapper.readTree(text);
            JsonNode dataNode = rootNode.path("data");
            if (dataNode.isMissingNode()){
                log.error("등기부등본의 data 필드 존재하지 않음");
                throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + InfraErrorMessages.PROPERTY_TITLE_ANALYSIS_ERROR);
            }
            contractResultRepository.updatePropertyTitleJson(userId, dataNode.toPrettyString());
        } catch (JsonProcessingException e){
            log.error("등기부등본 데이터 JSON 파싱 에러" + e.getMessage() + e);
            throw new IllegalArgumentException(ExceptionMessages.ERROR_PREFIX + InfraErrorMessages.PROPERTY_TITLE_ANALYSIS_ERROR);
        }
    }
}
