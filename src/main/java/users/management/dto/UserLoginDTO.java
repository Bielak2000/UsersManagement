package users.management.dto;

import lombok.AccessLevel;
import lombok.Builder;
import users.management.entity.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UserLoginDTO(@NotNull UUID id, @NotEmpty String email, @NotEmpty String password, boolean enabled,
                           @NotNull String role) {

    public static UserLoginDTO create(User user) {
        return UserLoginDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .role(user.getRole())
                .build();
    }

}
