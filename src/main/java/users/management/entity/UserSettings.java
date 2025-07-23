package users.management.entity;


import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import users.management.dto.UserSettingsFormDTO;

import java.util.UUID;

@Entity
@Getter
@Table(schema = "users", name = "user_settings")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users.user_settings SET deleted = true WHERE id =?")
public class UserSettings extends BaseEntity {

    private boolean smsNotification;
    private boolean appNotification;
    private boolean emailNotification;

    public UserSettings(UserSettingsFormDTO userSettingsFormDTO) {
        super(UUID.randomUUID());
        this.smsNotification = userSettingsFormDTO.smsNotification();
        this.emailNotification = userSettingsFormDTO.emailNotification();
        this.appNotification = userSettingsFormDTO.appNotification();
    }

    public void update(UserSettingsFormDTO userSettingsFormDTO) {
        this.smsNotification = userSettingsFormDTO.smsNotification();
        this.emailNotification = userSettingsFormDTO.emailNotification();
        this.appNotification = userSettingsFormDTO.appNotification();
    }

}
