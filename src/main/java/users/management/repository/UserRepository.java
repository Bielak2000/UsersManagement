package users.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import users.management.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findAllByCompanyId(UUID companyID);

}
