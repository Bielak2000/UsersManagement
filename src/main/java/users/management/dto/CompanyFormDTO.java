package users.management.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record CompanyFormDTO(@NotEmpty(message = "Name can't be empty") String name,
                             @NotEmpty(message = "TaxIdentifiaction password can't be empty") String taxIdentifiaction,
                             @NotEmpty(message = "PhoneNumber can't be empty") String phoneNumber,
                             @NotEmpty(message = "Email can't be emtpy") @Email String email,
                             @NotNull(message = "AddressFormDTO can't be null") AddressFormDTO addressFormDTO) {
}
