package users.management.service;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import users.management.dto.UserSettingsFormDTO;
import users.management.entity.User;
import users.management.entity.UserSettings;
import users.management.repository.UserSettingsRepository;

@AllArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    @Transactional
    public void updateUserSettings(User user, UserSettingsFormDTO userSettingsFormDTO) {
        UserSettings userSettings = user.getUserSettings();
        userSettings.update(userSettingsFormDTO);
        userSettingsRepository.save(userSettings);
    }

}
