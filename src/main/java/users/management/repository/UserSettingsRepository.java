package users.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import users.management.entity.UserSettings;

import java.util.UUID;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID> {

}
