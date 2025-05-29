package navi4.zipsa.infrastructure.api.clova.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record ContractAnalysisRequest(

        @NotBlank(message = "유저 아이디는 필수입니다.")
        Long userId,

        @NotBlank(message = "분석 파일은 필수입니다.")
        MultipartFile[] files
) {
}
