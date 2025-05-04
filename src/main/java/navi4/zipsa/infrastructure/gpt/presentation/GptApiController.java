package navi4.zipsa.infrastructure.gpt.presentation;

import lombok.RequiredArgsConstructor;
import navi4.zipsa.infrastructure.gpt.application.GptApiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/zipsa/external/gpt")
public class GptApiController {

    private final GptApiService gptApiService;

    @PostMapping("/chat")
    public Mono<String> publishGptChat(@RequestBody String question) {
        return gptApiService.chat(question);
    }
}
