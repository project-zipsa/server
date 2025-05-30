package navi4.zipsa.command.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import navi4.zipsa.command.JeonseContract.domain.ContractResult;
import navi4.zipsa.command.JeonseContract.domain.ContractResultRepository;
import navi4.zipsa.command.user.domain.User;
import navi4.zipsa.command.user.domain.UserRepository;
import navi4.zipsa.command.user.dto.LoginRequest;
import navi4.zipsa.command.user.dto.SignupRequest;
import navi4.zipsa.auth.utils.JwtProvider;
import navi4.zipsa.command.user.dto.SignupResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ContractResultRepository contractResultRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signUp(SignupRequest request) {
        validateLoginIdDuplicated(request.loginId());
        User user = User.create(request.loginId(), request.password(), request.userName());
        ContractResult contractResult  = ContractResult.create(user);
        userRepository.save(user);
        contractResultRepository.save(contractResult);
    }

    public String login(LoginRequest request) {
        User user = validateUser(request);
        return jwtProvider.createToken(user.getLoginId());
    }

    public SignupResponse getUserByLoginId(String loginId) {
        return SignupResponse.from(validateUserLoginId(loginId));
    }

    private void validateLoginIdDuplicated(String loginId){
        if (userRepository.existsByLoginId(loginId)){
            throw new IllegalArgumentException(loginId + "는 이미 존재하는 아이디입니다.");
        }
    }

    private User validateUser(LoginRequest request) {
        User user = validateUserLoginId(request.loginId());
        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    private User validateUserLoginId(String loginId) {
        return userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
    }

}
