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

    // 표제부
    @PostMapping("/br-title-info")
    public Mono<String> getBrTitleInfo(@RequestBody OdgDefaultRequest request) {
        try{
            return odgService.getOdgData(request, "getBrTitleInfo");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }

    // 총괄표제부
    @PostMapping("/br-recap-title-info")
    public Mono<String> getBrRecapTitleInfo(@RequestBody OdgDefaultRequest request) {
        try{
            return odgService.getOdgData(request, "getBrRecapTitleInfo");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }

    // 전유부
    @PostMapping("/br-expos-info")
    public Mono<String> getBrExposInfo(@RequestBody OdgDefaultRequest request) {
        try{
            return odgService.getOdgData(request, "getBrExposInfo");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }

    // 지역지구구역
    @PostMapping("/br-jijigu-info")
    public Mono<String> getBrJijiguInfo(@RequestBody OdgDefaultRequest request) {
        try{
            return odgService.getOdgData(request, "getBrJijiguInfo");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }
}
