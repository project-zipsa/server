package navi4.zipsa.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String userName);

    Optional<User> findUserByLoginId(String loginId);

    boolean existsByUserName(String userName);

    boolean existsByLoginId(String loginId);
}
