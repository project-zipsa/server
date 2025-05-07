package navi4.zipsa.infrastructure.api.odg.application;

import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.infrastructure.api.odg.dto.OdgDefaultRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
public class OdgService {

    private final WebClient webClient;
    private static final String ODG_BASE_URL = "https://apis.data.go.kr/1613000/BldRgstHubService";

    @Value("${odg.serviceKey}")
    private String ODG_SERVICE_KEY;

    public OdgService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(ODG_BASE_URL).build();
    }

    public Mono<String> getBrTitleInfo(OdgDefaultRequest request) {

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/BldRgstHubService/getBrTitleInfo")
                .queryParam("bjdongCd", request.bjdongCd())
                .queryParam("sigunguCd", request.sigunguCd())
                .queryParam("_type", "json");

        if (request.metadata() != null) {
            request.metadata().forEach((key, value) -> {
                if (value != null) {
                    builder.queryParam(key, value.toString());
                }
            });
        }

        String fullUri = builder.build().encode().toUriString() + "&serviceKey=" + ODG_SERVICE_KEY;
        log.info("[ODG] 최종 uri: {}", fullUri);
        return webClient.get()
                .uri(URI.create(fullUri))
                .retrieve()
                .bodyToMono(String.class);
    }
}
