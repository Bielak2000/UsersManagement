package users.management.service;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import users.management.dto.CompanyDTO;
import users.management.dto.CompanyFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.exception.NotFoundException;
import users.management.repository.CompanyRepository;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompanies() {
        return companyRepository.findAll().stream().map(CompanyDTO::create).toList();
    }

    @Transactional(readOnly = true)
    public Company getById(UUID companyID) {
        return companyRepository.findById(companyID).orElseThrow(() -> new NotFoundException(String.format("Company with id %s not found", companyID)));
    }

    @Transactional
    public Company create(CompanyFormDTO companyFormDTO, Address address) {
        Company company = new Company(companyFormDTO, address);
        companyRepository.save(company);
        return company;
    }

    @Transactional
    public Company update(UUID companyID, CompanyFormDTO companyFormDTO) {
        Company company = companyRepository.findById(companyID).orElseThrow(() -> new NotFoundException(String.format("Company with id %s not found", companyID)));
        Address address = company.getAddress().update(companyFormDTO.addressFormDTO());
        company.update(companyFormDTO, address);
        companyRepository.save(company);
        return company;
    }

}
