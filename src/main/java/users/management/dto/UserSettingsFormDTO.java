package users.management.dto;

import java.util.UUID;

public record UserSettingsFormDTO(UUID userId,
                                  boolean smsNotification,
                                  boolean appNotification,
                                  boolean emailNotification) {
}
