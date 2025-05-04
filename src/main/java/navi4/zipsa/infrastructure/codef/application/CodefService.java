package navi4.zipsa.infrastructure.codef.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodefService {

    private final CodefTokenPublisher tokenPublisher;
    private static final String GPT_API_BASE_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${codef.client.publicKey}")
    private String publicKey;

    @Value("${codef.client.encryptionTargetPlain}")
    private String encryptionTargetPlain;

    public Map<String, Object> getCodefAccessToken(){
        try {
            return tokenPublisher.publicAccessToken();
        } catch (Exception e) {
            System.err.println("Codef 액세스 토큰 발급 에러:  " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public String getCodefRSAPassword() throws Exception {
        return RSAEncryptor.encrypt(encryptionTargetPlain, publicKey);
    }
}
