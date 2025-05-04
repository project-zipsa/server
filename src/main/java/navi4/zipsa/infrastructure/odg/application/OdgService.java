package navi4.zipsa.infrastructure.odg.application;

import navi4.zipsa.infrastructure.odg.dto.OdgDefaultRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class OdgService {

    private final WebClient webClient;
    private static final String ODG_BASE_URL = "https://apis.data.go.kr/1613000/BldRgstHubService";

    @Value("${odg.serviceKey}")
    private String ODG_SERVICE_KEY;

    public OdgService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(ODG_BASE_URL).build();
    }

    public Mono<String> getBrTitleInfo(OdgDefaultRequest request) throws Exception {
            return webClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/getBrTitleInfo")
                                .queryParam("serviceKey", ODG_SERVICE_KEY)
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
                        return builder.build();
//                        URI finalUri = builder.build();
//                        System.out.println("최종 URI: " + finalUri); // 👈 URI 출력
//                        return finalUri;
                    })
                    .retrieve()
                    .bodyToMono(String.class);
    }


}
