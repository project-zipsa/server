package navi4.zipsa.infrastructure.api.codef.application;

import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.common.exception.ExceptionMessages;
import navi4.zipsa.infrastructure.exception.ApiExceptionMessages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Base64;
import java.util.HashMap;

@Slf4j
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
            log.error(ApiExceptionMessages.CODEF_TOKEN_REQUEST_FAILED, e.getResponseBodyAsString());
            throw new IllegalArgumentException(ApiExceptionMessages.CODEF_TOKEN_REQUEST_FAILED + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new IllegalArgumentException(ExceptionMessages.SERVER_ERROR + e.getMessage());
        }
    }

}
