package kr.co.ureca.Jungjuhyeon.user.infrastructure;

import kr.co.ureca.Jungjuhyeon.user.domin.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByNickName(String name);
}
