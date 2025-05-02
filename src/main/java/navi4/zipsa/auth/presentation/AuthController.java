package navi4.zipsa.auth.presentation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import navi4.zipsa.auth.application.UserService;
import navi4.zipsa.auth.dto.LoginRequest;
import navi4.zipsa.auth.dto.LoginResponse;
import navi4.zipsa.auth.dto.UserCreateRequest;
import navi4.zipsa.auth.dto.UserCreateResponse;
import navi4.zipsa.auth.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/zipsa/auth")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserCreateResponse> signup(@ModelAttribute @Valid final UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUp(request)); // 응답 통일
    }

    @PostMapping("/login")
    // RequestBody 랑 @ModelAttribute 차이
    public ResponseEntity<LoginResponse> login(@ModelAttribute @Valid final LoginRequest request) {
        userService.login(request);
        String token = jwtProvider.createToken(request.loginId());
        return ResponseEntity.ok(new LoginResponse(token)); // 응답 통일
    }
}
