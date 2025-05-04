package navi4.zipsa.infrastructure.gpt.application;

import java.util.List;
import java.util.Map;

public class GptChatRequestBuilder {

    public static Map<String, Object> buildChatRequest(String userQuestion) {
        return Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", userQuestion
                        )
                )
        );
    }
}
