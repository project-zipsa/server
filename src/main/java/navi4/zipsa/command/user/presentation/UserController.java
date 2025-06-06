package navi4.zipsa.command.user.presentation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import navi4.zipsa.command.user.application.UserService;
import navi4.zipsa.command.user.dto.*;
import navi4.zipsa.common.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<UserResponse>> getUserInfo(@RequestParam String loginId) {
        UserResponse response = userService.getUserByLoginId(loginId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.success(response));
    }

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<Void>> signup(@RequestBody @Valid final SignupRequest request) {
        userService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success("회원가입에 성공했습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid final LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(SuccessResponse.success("로그인에 성공했습니다.", new LoginResponse(token)));
    }

}
