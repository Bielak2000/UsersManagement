package users.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.management.dto.AddressFormDTO;
import users.management.dto.CompanyFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.exception.NotFoundException;
import users.management.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    private static final AddressFormDTO ADDRESS_FORM_DTO = new AddressFormDTO(UUID.randomUUID(), "town1", "postalCode1", "test1@test.pl", null, "city1", "street1");
    private static final Address ADDRESS = new Address(ADDRESS_FORM_DTO);
    private static final CompanyFormDTO COMPANY_FORM_DTO = new CompanyFormDTO("name1", "tax1", "123123121", "test1@test.pl", ADDRESS_FORM_DTO);

    @Test
    void getCompaniesShouldReturn() {
        // given
        List<Company> companies = List.of(
                new Company(COMPANY_FORM_DTO, ADDRESS)
        );

        // when
        when(companyRepository.findAll()).thenReturn(companies);

        // then
        Assertions.assertAll(
                () -> Assertions.assertEquals(companyService.getCompanies().get(0).taxIdentifiaction(), COMPANY_FORM_DTO.taxIdentifiaction()),
                () -> Assertions.assertEquals(companyService.getCompanies().get(0).email(), COMPANY_FORM_DTO.email()),
                () -> Assertions.assertEquals(companyService.getCompanies().get(0).addressDTO().city(), ADDRESS_FORM_DTO.city()),
                () -> Assertions.assertEquals(companyService.getCompanies().get(0).addressDTO().street(), ADDRESS_FORM_DTO.street())

        );
    }


    @Test
    void getCompanyByIdShouldReturn() {
        // given
        Company company = new Company(COMPANY_FORM_DTO, ADDRESS);

        // when
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));

        // then
        Assertions.assertEquals(companyService.getById(UUID.randomUUID()).getId(), company.getId());
    }

    @Test
    void getCompanyByIdShouldThrowNotFoundException() {
        // when
        when(companyRepository.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> companyService.getById(UUID.randomUUID()));
    }

    @Test
    void shouldCreateCompany() {
        // given
        Company company = new Company(COMPANY_FORM_DTO, ADDRESS);

        // when
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> companyService.create(COMPANY_FORM_DTO, ADDRESS)),
                () -> Assertions.assertEquals(companyService.create(COMPANY_FORM_DTO, ADDRESS).getTaxIdentifiaction(), COMPANY_FORM_DTO.taxIdentifiaction()),
                () -> Assertions.assertEquals(companyService.create(COMPANY_FORM_DTO, ADDRESS).getEmail(), COMPANY_FORM_DTO.email()),
                () -> Assertions.assertEquals(companyService.create(COMPANY_FORM_DTO, ADDRESS).getAddress().getCity(), ADDRESS_FORM_DTO.city()),
                () -> Assertions.assertEquals(companyService.create(COMPANY_FORM_DTO, ADDRESS).getAddress().getStreet(), ADDRESS_FORM_DTO.street())
        );
    }

    @Test
    void shouldUpdateCompany() {
        // given
        Company company = new Company(COMPANY_FORM_DTO, ADDRESS);
        AddressFormDTO newAddressFormDTO = new AddressFormDTO(UUID.randomUUID(), "newTown", "newPostalCode", "newTest@test.pl", null, "city1", "street1");
        Address newAddress = new Address(newAddressFormDTO);
        CompanyFormDTO newCompanyFormDTO = new CompanyFormDTO("newName", "newTax", "123123129", "newEmail@test.com", newAddressFormDTO);
        Company newCompany = new Company(newCompanyFormDTO, newAddress);

        // when
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(newCompany);

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> companyService.update(company.getId(), newCompanyFormDTO)),
                () -> Assertions.assertEquals(companyService.update(company.getId(), newCompanyFormDTO).getTaxIdentifiaction(), newCompanyFormDTO.taxIdentifiaction()),
                () -> Assertions.assertEquals(companyService.update(company.getId(), newCompanyFormDTO).getEmail(), newCompanyFormDTO.email()),
                () -> Assertions.assertEquals(companyService.update(company.getId(), newCompanyFormDTO).getAddress().getCity(), newAddressFormDTO.city()),
                () -> Assertions.assertEquals(companyService.update(company.getId(), newCompanyFormDTO).getAddress().getStreet(), newAddressFormDTO.street())
        );
    }

    @Test
    void updateCompanyThrowNotFoundException() {
        // when
        when(companyRepository.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> companyService.update(UUID.randomUUID(), COMPANY_FORM_DTO));
    }

}
