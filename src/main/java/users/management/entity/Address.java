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
import users.management.dto.AddressFormDTO;

import java.util.UUID;

@Entity
@Getter
@Table(schema = "users", name = "address")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users.address SET deleted = true WHERE id =?")
public class Address extends BaseEntity {

    private String town;
    private String postalCode;
    private String houseNumber;
    private String apartmentNumber;
    private String city;
    private String street;

    public Address(AddressFormDTO addressFormDTO) {
        super(UUID.randomUUID());
        this.town = addressFormDTO.town();
        this.postalCode = addressFormDTO.postalCode();
        this.houseNumber = addressFormDTO.houseNumber();
        this.apartmentNumber = addressFormDTO.apartmentNumber();
        this.city = addressFormDTO.city();
        this.street = addressFormDTO.street();
    }

    public Address update(AddressFormDTO addressFormDTO) {
        this.town = addressFormDTO.town();
        this.postalCode = addressFormDTO.postalCode();
        this.houseNumber = addressFormDTO.houseNumber();
        this.apartmentNumber = addressFormDTO.apartmentNumber();
        this.city = addressFormDTO.city();
        this.street = addressFormDTO.street();
        return this;
    }

}
