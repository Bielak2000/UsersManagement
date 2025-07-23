package users.management.entity;


import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import users.management.dto.UserFormDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(schema = "users", name = "users")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users.users SET deleted = true WHERE id =?")
public class User extends BaseEntity {

    private String name;
    private String surname;
    private String email;
    private String role;
    private String phoneNumber;
    @Setter
    private String password;
    @Setter
    private LocalDateTime lastActivityAt;
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public User(UserFormDTO userFormDTO, String password, Address address, Company company) {
        super(UUID.randomUUID());
        this.lastActivityAt = LocalDateTime.now();
        this.name = userFormDTO.name();
        this.role = userFormDTO.role();
        this.password = password;
        this.surname = userFormDTO.surname();
        this.email = userFormDTO.email();
        this.phoneNumber = userFormDTO.phoneNumber();
    }

    public void update(UserFormDTO userFormDTO, Address address, Company company) {
        this.name = userFormDTO.name();
        this.role = userFormDTO.role();
        this.surname = userFormDTO.surname();
        this.email = userFormDTO.email();
        this.phoneNumber = userFormDTO.phoneNumber();
        this.address = address;
        this.company = company;
    }

}
