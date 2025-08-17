package users.management.entity;


import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import users.management.dto.CompanyFormDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(schema = "users", name = "company")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users.company SET deleted = true WHERE id =?")
public class Company extends BaseEntity {

    private String name;
    private String taxIdentification;
    private String email;
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users;

    public Company(CompanyFormDTO companyFormDTO, Address address) {
        super(UUID.randomUUID());
        this.name = companyFormDTO.name();
        this.taxIdentification = companyFormDTO.taxIdentifiaction();
        this.email = companyFormDTO.email();
        this.phoneNumber = companyFormDTO.phoneNumber();
        this.address = address;
        this.users = new ArrayList<>();
    }

    public void update(CompanyFormDTO companyFormDTO, Address address) {
        this.name = companyFormDTO.name();
        this.taxIdentification = companyFormDTO.taxIdentifiaction();
        this.email = companyFormDTO.email();
        this.phoneNumber = companyFormDTO.phoneNumber();
        this.address = address;
    }
}
