package navi4.zipsa.infrastructure.api.odg.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.infrastructure.api.odg.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@Slf4j
@Service
public class OdgService {

    private static final String ODG_BASE_URL = "https://apis.data.go.kr/1613000/BldRgstHubService";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${odg.serviceKey}")
    private String ODG_SERVICE_KEY;

    public OdgService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(ODG_BASE_URL).build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<TotalBrInfoRequest> getTotalBrInfoRequest(OdgDefaultRequest request) {
        Mono<BrTitleInfoRequest> titleMono = getRequiredBrTitleInfo(request);
        Mono<BrExposInfoRequest> exposMono = getRequiredBrExposInfo(request);
        Mono<BrJijiguInfoRequest> jijiguMono = getRequiredBrJijiguInfo(request);

        return Mono.zip(titleMono, exposMono, jijiguMono)
                .map(tuple -> new TotalBrInfoRequest(
                        tuple.getT1(),
                        tuple.getT2(),
                        tuple.getT3()
                ));
    }

    private Mono<BrTitleInfoRequest> getRequiredBrTitleInfo(OdgDefaultRequest request) {
        Mono<String> response = requestOdgData(request, "getBrTitleInfo");
        return toBrTitleInfoRequest(response);
    }

    // 전유부
    private Mono<BrExposInfoRequest> getRequiredBrExposInfo(OdgDefaultRequest request) {
        Mono<String> response = requestOdgData(request, "getBrExposInfo");
        return toBrExposInfo(response);
    }

    // 지역지구구역
    private Mono<BrJijiguInfoRequest> getRequiredBrJijiguInfo(OdgDefaultRequest request) {
        Mono<String> response = requestOdgData(request, "getBrJijiguInfo");
        return toBrJijiguInfo(response);
    }

    private Mono<String> requestOdgData(OdgDefaultRequest request, String requestUri) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("apis.data.go.kr")
                .path("/1613000/BldRgstHubService/" + requestUri)
                .queryParam("bjdongCd", request.bjdongCd())
                .queryParam("sigunguCd", request.sigunguCd())
                .queryParam("_type", "json");

        String fullUri = builder.build().encode().toUriString() + "&serviceKey=" + ODG_SERVICE_KEY;
        log.info("[ODG] 최종 uri: {}", fullUri);
        return webClient.get()
                .uri(URI.create(fullUri))
                .retrieve()
                .bodyToMono(String.class);
    }

    // 표제부 dto 생성
    private Mono<BrTitleInfoRequest> toBrTitleInfoRequest(Mono<String> data){
        return extractItemAndConvert(data, item -> new BrTitleInfoRequest(
                        getSafe(item, "platPlc"),
                        getSafe(item, "bun"),
                        getSafe(item, "ji"),
                        getSafe(item, "newPlatPlc"),
                        getSafe(item, "bldNm"),
                        getSafe(item, "mainPurpsCdNm"),
                        getSafe(item, "etcPurps"),
                        getSafe(item, "archArea"),
                        getSafe(item, "totArea"),
                        getSafe(item, "grndFlrCnt"),
                        getSafe(item, "ugrndFlrCnt"),
                        getSafe(item, "strctCdNm"),
                        getSafe(item, "etcStrct"),
                        getSafe(item, "useAprDay")
        ));
    }

    // 전유부 dto 생성
    private Mono<BrExposInfoRequest> toBrExposInfo(Mono<String> data){
        return extractItemAndConvert(data, item -> new BrExposInfoRequest(
                        getSafe(item, "dongNm"),
                        getSafe(item, "hoNm"),
                        getSafe(item, "flrNo")
        ));
    }

    // 지역지구구역 dto 생성
    private Mono<BrJijiguInfoRequest> toBrJijiguInfo(Mono<String> data){
        return extractItemAndConvert(data, item -> new BrJijiguInfoRequest(
                getSafe(item, "jijiguGbCdNm"),
                getSafe(item, "jijiguCdNm")
        ));
    }

    private <T> Mono<T> extractItemAndConvert(Mono<String> data, Function<JsonNode, T> mapperFunction){
        return data.map(json -> {
            try{
                JsonNode root = objectMapper.readTree(json);
                JsonNode item = root
                        .path("response")
                        .path("body")
                        .path("items")
                        .path("item");

                if (item.isArray()){
                    item = item.get(0);
                }
                return mapperFunction.apply(item);

            } catch (JsonProcessingException e){
                throw new IllegalArgumentException("[ODG] 데이터 파싱 실패", e);
            }
        });
    }

    private String getSafe(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        return value.isMissingNode() || value.isNull() ? null : value.asText();
    }

}