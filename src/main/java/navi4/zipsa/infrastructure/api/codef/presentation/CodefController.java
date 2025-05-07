package navi4.zipsa.infrastructure.api.codef.presentation;

import lombok.RequiredArgsConstructor;
import navi4.zipsa.infrastructure.api.codef.application.CodefService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/zipsa/external/codef")
public class CodefController {

    private final CodefService apiService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> publishCodefAccessToken() {
        Map<String, Object> tokenMap = apiService.getCodefAccessToken();
        return ResponseEntity.ok(tokenMap);
    }

    @GetMapping("/rsa-password")
    public String publishRsaPassword() throws Exception {
        return apiService.getCodefRSAPassword();
    }



}
