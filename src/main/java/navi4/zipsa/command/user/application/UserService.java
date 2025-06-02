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
import navi4.zipsa.command.user.exception.UserExceptionMessages;
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
        userRepository.save(user);
        userRepository.flush();
        ContractResult contractResult  = ContractResult.create(user);
        contractResultRepository.save(contractResult);
        contractResultRepository.flush();
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
            throw new IllegalArgumentException(loginId + UserExceptionMessages.DUPLICATED_LOGIN_ID);
        }
    }

    private User validateUser(LoginRequest request) {
        User user = validateUserLoginId(request.loginId());
        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException(UserExceptionMessages.WRONG_PASSWORD);
        }
        return user;
    }

    private User validateUserLoginId(String loginId) {
        return userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(UserExceptionMessages.NOT_EXIST_LOGIN_ID));
    }

}
