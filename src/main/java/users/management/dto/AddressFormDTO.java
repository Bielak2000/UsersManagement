package users.management.dto;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import users.management.entity.Address;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record AddressFormDTO(@Nullable UUID addressID,
                             @NotEmpty(message = "Town can't be empty") String town,
                             @NotEmpty(message = "PostalCode can't be empty") String postalCode,
                             @NotEmpty(message = "HouseNumber can't be empty") String houseNumber,
                             @Nullable String apartmentNumber,
                             @NotEmpty(message = "City can't be empty") String city,
                             @NotEmpty(message = "Street can't be empty") String street) {

    public static AddressFormDTO create(Address address) {
        return address != null ? AddressFormDTO.builder()
                .town(address.getTown())
                .postalCode(address.getPostalCode())
                .houseNumber(address.getHouseNumber())
                .apartmentNumber(address.getApartmentNumber())
                .city(address.getCity())
                .street(address.getStreet())
                .build() : null;
    }

}
