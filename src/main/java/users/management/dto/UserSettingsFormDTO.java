package users.management.dto;

import lombok.AccessLevel;
import lombok.Builder;
import users.management.entity.UserSettings;

@Builder(access = AccessLevel.PRIVATE)
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
