package users.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import lombok.Builder;
import users.management.entity.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserDTO(@NotNull UUID id,
                      @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                      @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime lastActivityAt,
                      @NotEmpty(message = "Name can't be empty") String name,
                      @NotEmpty(message = "Surname can't be empty") String surname,
                      @NotEmpty(message = "Email can't be emtpy") String email,
                      @Nullable String phoneNumber,
                      @Nullable CompanyDTO companyDTO,
                      @Nullable AddressFormDTO addressDTO,
                      @NotNull UserSettingsFormDTO userSettingsFormDTO) {
    public static UserDTO create(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .lastActivityAt(user.getLastActivityAt())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .companyDTO(CompanyDTO.create(user.getCompany()))
                .addressDTO(AddressFormDTO.create(user.getAddress()))
                .userSettingsFormDTO(UserSettingsFormDTO.create(user.getUserSettings()))
                .build();
    }
}
