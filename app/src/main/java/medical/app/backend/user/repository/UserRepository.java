package medical.app.backend.user.repository;

import medical.app.backend.common.model.person.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByExternalId(String externalId);

    boolean existsByExternalId(String externalId);
}
