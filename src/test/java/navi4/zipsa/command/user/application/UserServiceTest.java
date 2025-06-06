package navi4.zipsa.command.user.application;

import jakarta.transaction.Transactional;
import navi4.zipsa.command.user.dto.LoginRequest;
import navi4.zipsa.command.user.dto.SignupRequest;
import navi4.zipsa.command.user.dto.UserResponse;
import navi4.zipsa.command.user.exception.UserExceptionMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private String loginId;
    private String username;
    private String password;
    private SignupRequest signupRequest;

    @BeforeEach
    void userSetUP(){
        loginId = "zipsaUser01";
        username = "김집사";
        password = "Zipsa2024!";
        signupRequest = new SignupRequest(loginId, password, username);
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccessTest(){
        // when
        userService.signUp(signupRequest);
        UserResponse response = userService.getUserByLoginId(loginId);

        //then
        assertThat(response.loginId()).isEqualTo(loginId);
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.password()).isEqualTo(password);
    }


    @Nested
    @DisplayName("회원가입 실패")
    class SignupFailTest{
        @Test
        @DisplayName("중복된 아이디")
        void duplicatedLoginId(){
            userService.signUp(signupRequest);
            String loginId2 = "zipsaUser01";
            String username2 = "박집사";
            String password2 = "Zipsa2024!";

            assertThatThrownBy(
                    () -> userService.signUp(new SignupRequest(loginId2, username2, password2))
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(UserExceptionMessages.DUPLICATED_LOGIN_ID);
        }
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest(){
        // given
        String loginId = "zipsaUser01";
        String password = "Zipsa2024!";
        LoginRequest loginRequest = new LoginRequest(loginId, password);

        // when
        userService.signUp(signupRequest);

        // 예외를 던지지 않는다
        assertDoesNotThrow(() -> {
            userService.login(loginRequest);
        });
    }

    // 클래스
    @Nested
    @DisplayName("로그인 실패")
    class LoginFailTest{

        @Test
        @DisplayName("존재하지 않는 로그인 아이디")
        void notExistLoginId(){
            userService.signUp(signupRequest);

            String loginId = "zipsaUser02";
            String password = "Zipsa2024!";
            assertThatThrownBy(
                    () -> userService.login(new LoginRequest(loginId, password))
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(UserExceptionMessages.NOT_EXIST_LOGIN_ID);
        }

        @Test
        @DisplayName("잘못된 비밀번호")
        void wrongPassword(){
            userService.signUp(signupRequest);

            String loginId = "zipsaUser01";
            String password = "Zipsa2024";
            assertThatThrownBy(
                    () -> userService.login(new LoginRequest(loginId, password))
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(UserExceptionMessages.WRONG_PASSWORD);
        }
    }

}
