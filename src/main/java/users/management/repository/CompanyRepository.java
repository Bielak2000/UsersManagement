package users.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import users.management.entity.Company;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

}
