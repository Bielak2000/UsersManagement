package users.management.dto;

import javax.validation.constraints.NotEmpty;

public record ChangePasswordDTO(@NotEmpty(message = "Old password can't be empty") String oldPassword,
                                @NotEmpty(message = "New password can't be empty") String newPassword,
                                @NotEmpty(message = "Confirmation password can't be empty") String confirmationPassword) {
}
