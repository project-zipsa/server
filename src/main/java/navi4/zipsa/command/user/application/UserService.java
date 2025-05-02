package navi4.zipsa.command.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import navi4.zipsa.command.user.domain.User;
import navi4.zipsa.command.user.domain.UserRepository;
import navi4.zipsa.command.user.dto.LoginRequest;
import navi4.zipsa.command.user.dto.UserCreateRequest;
import navi4.zipsa.auth.utils.JwtProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signUp(UserCreateRequest request) {
        validateLoginIdDuplicated(request.loginId());
        //validateUserNameDuplicated(request.name());

        User user = User.create(request.loginId(), request.password(), request.userName());
        userRepository.save(user);
    }

    private void validateLoginIdDuplicated(String loginId){
        if (userRepository.existsByLoginId(loginId)){
            throw new IllegalArgumentException(loginId + "는 이미 존재하는 아이디입니다.");
        }
    }

//    private void validateUserNameDuplicated(String userName){
//        if (userRepository.existsByName(userName)){
//            throw new IllegalArgumentException(userName + "는 이미 존재하는 사용자 입니다.");
//        }
//    }

    public String login(LoginRequest request) {
        User user = validateUser(request);
        return jwtProvider.createToken(user.getLoginId());
    }

    private User validateUser(LoginRequest request) {
        User user = userRepository.findUserByLoginId(request.loginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

}
