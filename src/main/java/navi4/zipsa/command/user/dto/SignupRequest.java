package navi4.zipsa.command.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import navi4.zipsa.command.user.exception.UserExceptionMessages;

public record SignupRequest (

        @NotBlank(message = "아이디는 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$", message = UserExceptionMessages.INVALID_SIGNUP_LOGIN_ID)
        String loginId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
                message = UserExceptionMessages.INVALID_SIGNUP_PASSWORD
        )
        String password,

        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Size(min = 2, max = 10, message = UserExceptionMessages.INVALID_SIGNUP_USERNAME)
        String userName
){}
