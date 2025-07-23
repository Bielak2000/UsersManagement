package users.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import users.management.entity.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
