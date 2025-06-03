package navi4.zipsa.command.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import navi4.zipsa.command.user.exception.UserExceptionMessages;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SignupRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("회원가입 비정상 요청")
    class InvalidSignupRequest {

        @Test
        @DisplayName("로그인 아이디 규약 위반-특수문자 포함")
        void invalidLoginId1(){
            // given
            String loginId = "user!";
            String password = "Test@2024";
            String username = "김집사";

            // when
            SignupRequest request = new SignupRequest(loginId, password, username);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

            // then
            assertThat(violations)
                    .anyMatch(v -> v.getMessage().contains(UserExceptionMessages.INVALID_SIGNUP_LOGIN_ID));
        }

        @Test
        @DisplayName("로그인 아이디 규약 위반-길이 미충족")
        void invalidLoginId2(){
            // given
            String loginId = "user";
            String password = "Test@2024";
            String username = "김집사";

            // when
            SignupRequest request = new SignupRequest(loginId, password, username);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

            // then
            assertThat(violations)
                    .anyMatch(v -> v.getMessage().contains(UserExceptionMessages.INVALID_SIGNUP_LOGIN_ID));
        }

        @Test
        @DisplayName("비밀번호 규약 위반-특수문자 미포함")
        void invalidPassword1(){
            // given
            String loginId = "zipsazip12";
            String password = "password123";
            String username = "김집사";

            // when
            SignupRequest request = new SignupRequest(loginId, password, username);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

            // then
            assertThat(violations)
                    .anyMatch(v -> v.getMessage().contains(UserExceptionMessages.INVALID_SIGNUP_PASSWORD));
        }

        @Test
        @DisplayName("비밀번호 규약 위반-숫자 미포함")
        void invalidPassword2(){
            // given
            String loginId = "zipsazip12";
            String password = "password!!";
            String username = "김집사";

            // when
            SignupRequest request = new SignupRequest(loginId, password, username);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

            // then
            assertThat(violations)
                    .anyMatch(v -> v.getMessage().contains(UserExceptionMessages.INVALID_SIGNUP_PASSWORD));
        }

        @Test
        @DisplayName("비밀번호 규약 위반-길이 미충족")
        void invalidPassword3(){
            // given
            String loginId = "zipsazip12";
            String password = "pass";
            String username = "김집사";

            // when
            SignupRequest request = new SignupRequest(loginId, password, username);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

            // then
            assertThat(violations)
                    .anyMatch(v -> v.getMessage().contains(UserExceptionMessages.INVALID_SIGNUP_PASSWORD));
        }
    }
}
