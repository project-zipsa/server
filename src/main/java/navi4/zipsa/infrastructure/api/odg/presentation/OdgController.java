package navi4.zipsa.infrastructure.api.odg.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import navi4.zipsa.infrastructure.api.odg.application.OdgService;
import navi4.zipsa.infrastructure.api.odg.dto.OdgDefaultRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/zipsa/external/odg")
public class OdgController {

    private final OdgService odgService;

    @PostMapping("/br-title-info")
    public Mono<String> getBrTitleInfo(@RequestBody OdgDefaultRequest request) {
        try{
            return odgService.getBrTitleInfo(request);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }
}
