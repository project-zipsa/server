package navi4.zipsa.infrastructure.odg.presentation;

import lombok.RequiredArgsConstructor;
import navi4.zipsa.infrastructure.odg.application.OdgService;
import navi4.zipsa.infrastructure.odg.dto.OdgDefaultRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/zipsa/external/odg")
public class OdgController {

    private final OdgService odgService;

    @GetMapping("/br-title-info")
    public Mono<String> getBrTitleInfo(@RequestBody OdgDefaultRequest request) {
        try{
            return odgService.getBrTitleInfo(request);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return Mono.error(e);
        }
    }


}
