package users.management.dto;

import lombok.Builder;
import users.management.entity.Company;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record CompanyDTO(@NotEmpty(message = "Name can't be empty") String name,
                         @NotEmpty(message = "TaxIdentifiaction password can't be empty") String taxIdentifiaction,
                         @NotEmpty(message = "PhoneNumber can't be empty") String phoneNumber,
                         @NotEmpty(message = "Email can't be emtpy") @Email String email,
                         @NotNull AddressFormDTO addressDTO) {

    public static CompanyDTO create(Company company) {
        return company != null ?
                CompanyDTO.builder()
                        .name(company.getName())
                        .taxIdentifiaction(company.getTaxIdentifiaction())
                        .phoneNumber(company.getPhoneNumber())
                        .email(company.getEmail())
                        .addressDTO(AddressFormDTO.create(company.getAddress()))
                        .build() : null;
    }

}
