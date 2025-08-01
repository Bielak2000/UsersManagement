package users.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.management.dto.AddressFormDTO;
import users.management.dto.ChangePasswordDTO;
import users.management.dto.CompanyDTO;
import users.management.dto.CompanyFormDTO;
import users.management.dto.UserDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserSettingsFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.entity.User;
import users.management.service.AddressService;
import users.management.service.CompanyService;
import users.management.service.UserService;
import users.management.service.UserSettingsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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
    private static final UserFormDTO USER_FORM_DTO = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", UUID.randomUUID(), ADDRESS_FORM_DTO, USER_SETTINGS_FORM_DTO);


    @Test
    public void createCompanyShouldNotThrow() {
        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.createCompany(COMPANY_FORM_DTO));
    }

    @Test
    public void createUserWithExistsAddressShouldNotThrow() {
        // when
        when(companyService.getById(any())).thenReturn(new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO)));
        when(addressService.getById(any(UUID.class))).thenReturn(new Address(ADDRESS_FORM_DTO));
        doNothing().when(userService).create(any(UserFormDTO.class), any(Address.class), any(Company.class));

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.createUser(USER_FORM_DTO));
    }

    @Test
    public void createUserWithNotExistsAddressShouldNotThrow() {
        // given
        AddressFormDTO addressFormDTO = new AddressFormDTO(null, "town", "postalCode", "houseNumber", "apartmentNumber", "city", "street");
        UserFormDTO userFormDTO = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", UUID.randomUUID(), addressFormDTO, USER_SETTINGS_FORM_DTO);

        // when
        when(companyService.getById(any())).thenReturn(new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO)));
        doNothing().when(userService).create(any(UserFormDTO.class), any(Address.class), any(Company.class));

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.createUser(userFormDTO));
    }

    @Test
    public void createUserWithoutAddressShouldNotThrow() {
        // given
        UserFormDTO userFormDTO = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", UUID.randomUUID(), null, USER_SETTINGS_FORM_DTO);

        // when
        when(companyService.getById(any())).thenReturn(new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO)));
        doNothing().when(userService).create(any(UserFormDTO.class), isNull(), any(Company.class));

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.createUser(userFormDTO));
    }

    @Test
    public void createUserWithoutCompanyShouldNotThrow() {
        // given
        UserFormDTO userFormDTO = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", null, ADDRESS_FORM_DTO, USER_SETTINGS_FORM_DTO);

        // when
        when(addressService.getById(any(UUID.class))).thenReturn(new Address(ADDRESS_FORM_DTO));
        doNothing().when(userService).create(any(UserFormDTO.class), any(Address.class), isNull());

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.createUser(userFormDTO));
    }

    @Test
    public void updateCompanyShouldNotThrow() {
        // given
        Company company = new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO));

        // when
        when(companyService.update(any(UUID.class), any(CompanyFormDTO.class))).thenReturn(company);

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.updateCompany(UUID.randomUUID(), COMPANY_FORM_DTO));
    }

    @Test
    public void updateUserShouldNotThrow() {
        // given
        Company company = new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO));

        // when
        when(companyService.getById(any(UUID.class))).thenReturn(company);

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.updateUser(USER_FORM_DTO.email(), USER_FORM_DTO));
    }

    @Test
    public void updateUserWithoutCompanyShouldNotThrow() {
        // given
        UserFormDTO userFormDTO = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", null, ADDRESS_FORM_DTO, USER_SETTINGS_FORM_DTO);

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.updateUser(userFormDTO.email(), userFormDTO));
    }

    @Test
    public void updateLastActivityUserShouldNotThrow() {
        // given
        String email = "email";

        // when
        doNothing().when(userService).updateLastActivityUser(any(String.class));

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.updateLastActivityUser(email));
    }

    @Test
    public void updateUserSettingsShouldNotThrow() {
        // given
        Company company = new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO));
        User user = new User(USER_FORM_DTO, "password", new Address(ADDRESS_FORM_DTO), company);

        // when
        when(userService.getUserByEmail(any(String.class))).thenReturn(user);
        doNothing().when(userSettingsService).updateUserSettings(user, USER_SETTINGS_FORM_DTO);

        // then
        Assertions.assertDoesNotThrow(() -> usersManagementFacade.updateUserSettings(user.getEmail(), USER_SETTINGS_FORM_DTO));
    }

    @Test
    public void getCompaniesShouldReturn() {
        // given
        CompanyDTO companyDTO = new CompanyDTO("name", "tax", "123123123", "email", ADDRESS_FORM_DTO);

        // when
        when(companyService.getCompanies()).thenReturn(List.of(companyDTO));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> usersManagementFacade.getCompanies()),
                () -> Assertions.assertEquals(usersManagementFacade.getCompanies().get(0).email(), companyDTO.email()),
                () -> Assertions.assertEquals(usersManagementFacade.getCompanies().get(0).addressDTO().addressID(), companyDTO.addressDTO().addressID()),
                () -> Assertions.assertEquals(usersManagementFacade.getCompanies().get(0).name(), companyDTO.name())
        );
    }

    @Test
    public void getUsersShouldReturn() {
        // given
        CompanyDTO companyDTO = new CompanyDTO("name", "tax", "123123123", "email", ADDRESS_FORM_DTO);
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), LocalDateTime.now().minusDays(1), LocalDateTime.now(), "name", "surname", "email", "123123123",
                companyDTO, ADDRESS_FORM_DTO, USER_SETTINGS_FORM_DTO);

        // when
        when(userService.getUsers()).thenReturn(List.of(userDTO));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> usersManagementFacade.getUsers()),
                () -> Assertions.assertEquals(usersManagementFacade.getUsers().get(0).name(), userDTO.name()),
                () -> Assertions.assertEquals(usersManagementFacade.getUsers().get(0).addressDTO().addressID(), userDTO.addressDTO().addressID()),
                () -> Assertions.assertEquals(usersManagementFacade.getUsers().get(0).companyDTO().name(), companyDTO.name())
        );
    }

    @Test
    public void getUsersByCompanyIdShouldReturn() {
        // given
        CompanyDTO companyDTO = new CompanyDTO("name", "tax", "123123123", "email", ADDRESS_FORM_DTO);
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), LocalDateTime.now().minusDays(1), LocalDateTime.now(), "name", "surname", "email", "123123123",
                companyDTO, ADDRESS_FORM_DTO, USER_SETTINGS_FORM_DTO);

        // when
        when(userService.getUsersByCompanyId(any(UUID.class))).thenReturn(List.of(userDTO));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> usersManagementFacade.getUsersByCompanyId(UUID.randomUUID())),
                () -> Assertions.assertEquals(usersManagementFacade.getUsersByCompanyId(UUID.randomUUID()).get(0).name(), userDTO.name()),
                () -> Assertions.assertEquals(usersManagementFacade.getUsersByCompanyId(UUID.randomUUID()).get(0).addressDTO().addressID(), userDTO.addressDTO().addressID()),
                () -> Assertions.assertEquals(usersManagementFacade.getUsersByCompanyId(UUID.randomUUID()).get(0).companyDTO().name(), companyDTO.name())
        );
    }

    @Test
    public void changeUserPasswordShouldChange() {
        // given
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPass", "newPass", "newPass");

        // when
        doNothing().when(userService).changePassword(any(UUID.class), any(ChangePasswordDTO.class));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> usersManagementFacade.changeUserPassword(UUID.randomUUID(), changePasswordDTO))
        );
    }

}
