package navi4.zipsa.infrastructure.api.gpt.application;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GptApiService {

    private final WebClient webClient;

    public GptApiService(WebClient.Builder builder,
                         @Value("${gpt.url}") String gptApiBaseUrl,
                         @Value("${gpt.key}") String gptApiKey) {
        this.webClient = builder
                .baseUrl(gptApiBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gptApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<String> chat(String userQuestion) {
        Map<String, Object> body = GptChatRequestBuilder.build(userQuestion); // TODO: 타입안정성 보장 - dto로 받기
        return webClient
                .post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GptChatResponse.class)
                .map(response -> response.getChoices().get(0).message().content());
    }

    // TODO: NPE 검사
    @Getter
    @NoArgsConstructor
    private static class GptChatResponse {
        private List<Choice> choices;
        public record Choice(Message message) {}
        public record Message(String content){}
    }
}
