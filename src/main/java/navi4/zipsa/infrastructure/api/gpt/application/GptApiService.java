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
    private static final String GPT_API_BASE_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${gpt.key}")
    private String GPT_API_KEY;

    public GptApiService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(GPT_API_BASE_URL).build();
    }

    public Mono<String> chat(String userQuestion) {
        Map<String, Object> body = GptChatRequestBuilder.build(userQuestion);

        return webClient
                .post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + GPT_API_KEY)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GptChatResponse.class)
                .map(response -> response.getChoices().get(0).message().content());
    }

    @Getter
    @NoArgsConstructor
    private static class GptChatResponse {
        private List<Choice> choices;
        public record Choice(Message message) {}
        public record Message(String content){}
    }
}
