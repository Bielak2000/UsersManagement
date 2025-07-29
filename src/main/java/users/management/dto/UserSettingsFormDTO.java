package users.management.dto;

import lombok.Builder;
import users.management.entity.UserSettings;

@Builder
public record UserSettingsFormDTO(boolean smsNotification,
                                  boolean appNotification,
                                  boolean emailNotification) {

    public static UserSettingsFormDTO create(UserSettings userSettings) {
        return UserSettingsFormDTO.builder()
                .smsNotification(userSettings.isSmsNotification())
                .appNotification(userSettings.isAppNotification())
                .emailNotification(userSettings.isEmailNotification())
                .build();
    }

}
