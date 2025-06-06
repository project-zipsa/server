package navi4.zipsa.command.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
