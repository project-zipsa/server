package navi4.zipsa.infrastructure.codef.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Base64;
import java.util.HashMap;

@Component
public class CodefTokenPublisher {

    private static final String CODEF_OAUTH_ENDPOINT = "https://oauth.codef.io";
    private static final String CODEF_TOKEN_URL_PATH = "/oauth/token";
    private static final String CODEF_TOKEN_REQUEST_BODY = "grant_type=client_credentials&scope=read";

    @Value("${codef.client.id}")
    private String CLIENT_ID;

    @Value("${codef.client.secret}")
    private String CLIENT_SECRET;

    private final WebClient webClient;

    public CodefTokenPublisher() {
        this.webClient = WebClient.builder()
                .baseUrl(CODEF_OAUTH_ENDPOINT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    public HashMap<String, Object> publicAccessToken(){
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        try {
            return webClient.post()
                    .uri(CODEF_TOKEN_URL_PATH)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                    .bodyValue(CODEF_TOKEN_REQUEST_BODY)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<HashMap<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            System.err.println("Codef 토큰 요청 실패: " + e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            System.out.println("서버 에러: " + e.getMessage());
            return null;
        }
    }

}
