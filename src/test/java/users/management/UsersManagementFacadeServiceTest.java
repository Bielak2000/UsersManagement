package users.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import users.management.dto.AddressFormDTO;
import users.management.dto.CompanyFormDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserSettingsFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.service.AddressService;
import users.management.service.CompanyService;
import users.management.service.UserService;
import users.management.service.UserSettingsService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersManagementFacadeServiceTest {

    @Mock
    UserService userService;
    @Mock
    UserSettingsService userSettingsService;
    @Mock
    CompanyService companyService;
    @Mock
    AddressService addressService;
    @InjectMocks
    UsersManagementFacade usersManagementFacade;

    private static final UserSettingsFormDTO USER_SETTINGS_FORM_DTO = new UserSettingsFormDTO(false, true, false);
    private static final AddressFormDTO ADDRESS_FORM_DTO = new AddressFormDTO(UUID.randomUUID(), "town", "postalCode", "houseNumber", "apartmentNumber", "city", "street");
    private static final CompanyFormDTO COMPANY_FORM_DTO = new CompanyFormDTO("name", "tax", "123123123", "email", ADDRESS_FORM_DTO);

    @Test
    public void createCompanyShouldNotThrow() {
        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.createCompany(COMPANY_FORM_DTO));
    }

    @Test
    public void createUserWithCompanyShouldNotThrow() {
        // given
        UserFormDTO userFormDTO =  new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", UUID.randomUUID(), ADDRESS_FORM_DTO, USER_SETTINGS_FORM_DTO);

        // when
        when(companyService.getById(any())).thenReturn(new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO)));
        doNothing().when(userService).create(any(UserFormDTO.class), any(Address.class), any(Company.class));

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.createUser(userFormDTO));
    }

}
