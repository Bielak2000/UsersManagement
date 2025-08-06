package users.management.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record ResetPasswordDTO(@NotEmpty(message = "Password can't be empty")
                               @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "The password does not meet the requirements") String password,
                               @NotEmpty(message = "ConfirmationPassword can't be empty")
                               @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "The password does not meet the requirements") String confirmationPassword,
                               @NotEmpty(message = "Token can't be empty") String token) {
}
