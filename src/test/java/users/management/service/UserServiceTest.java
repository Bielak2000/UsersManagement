package users.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import users.management.dto.AddressFormDTO;
import users.management.dto.ChangePasswordDTO;
import users.management.dto.CompanyFormDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserSettingsFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.entity.User;
import users.management.exception.BadRequestException;
import users.management.exception.NotFoundException;
import users.management.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    private static final List<AddressFormDTO> ADDRESS_FORM_DTO_LIST = List.of(
            new AddressFormDTO(UUID.randomUUID(), "town1", "postalCode1", "test1@test.pl", null, "city1", "street1"),
            new AddressFormDTO(UUID.randomUUID(), "town2", "postalCode2", "test2@test.pl", null, "city2", "street2"),
            new AddressFormDTO(UUID.randomUUID(), "town3", "postalCode3", "test3@test.pl", null, "city3", "street3")
    );


    private static final List<CompanyFormDTO> COMPANY_DTO_LIST = List.of(
            new CompanyFormDTO("name1", "tax1", "123123111", "email1@test.pl", ADDRESS_FORM_DTO_LIST.get(0)),
            new CompanyFormDTO("name2", "tax2", "123123112", "email2@test.pl", ADDRESS_FORM_DTO_LIST.get(1)),
            new CompanyFormDTO("name3", "tax3", "123123113", "email3@test.pl", ADDRESS_FORM_DTO_LIST.get(2))
    );

    private static final List<Company> COMPANY_LIST = List.of(
            new Company(COMPANY_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0))),
            new Company(COMPANY_DTO_LIST.get(1), new Address(ADDRESS_FORM_DTO_LIST.get(1))),
            new Company(COMPANY_DTO_LIST.get(2), new Address(ADDRESS_FORM_DTO_LIST.get(2)))
    );

    private static final List<UserSettingsFormDTO> USER_SETTINGS_FORM_DTO_LIST = List.of(
            new UserSettingsFormDTO(false, true, false),
            new UserSettingsFormDTO(false, true, false),
            new UserSettingsFormDTO(true, true, true)
    );

    private static final List<UserFormDTO> USER_FORM_DTO_LIST = List.of(
            new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", COMPANY_LIST.get(0).getId(), ADDRESS_FORM_DTO_LIST.get(0), USER_SETTINGS_FORM_DTO_LIST.get(0)),
            new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", COMPANY_LIST.get(1).getId(), ADDRESS_FORM_DTO_LIST.get(0), USER_SETTINGS_FORM_DTO_LIST.get(1)),
            new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", COMPANY_LIST.get(2).getId(), ADDRESS_FORM_DTO_LIST.get(0), USER_SETTINGS_FORM_DTO_LIST.get(2))
    );

    private static final ChangePasswordDTO CHANGE_PASSWORD_DTO = new ChangePasswordDTO("oldPass", "newPass", "new Pass");

    @Test
    void getUsersShouldReturn() {
        // given
        List<User> users = List.of(
                new User(USER_FORM_DTO_LIST.get(0), "password1", new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0)),
                new User(USER_FORM_DTO_LIST.get(1), "password1", new Address(ADDRESS_FORM_DTO_LIST.get(1)), COMPANY_LIST.get(1)),
                new User(USER_FORM_DTO_LIST.get(2), "password1", new Address(ADDRESS_FORM_DTO_LIST.get(2)), COMPANY_LIST.get(2))
        );

        // when
        when(userRepository.findAll()).thenReturn(users);

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.getUsers()),
                () -> Assertions.assertEquals(userService.getUsers().size(), 3),
                () -> Assertions.assertEquals(userService.getUsers().get(0).name(), USER_FORM_DTO_LIST.get(0).name()),
                () -> Assertions.assertEquals(userService.getUsers().get(0).email(), USER_FORM_DTO_LIST.get(0).email()),
                () -> Assertions.assertNotNull(userService.getUsers().get(0).addressDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(0).addressDTO().city(), ADDRESS_FORM_DTO_LIST.get(0).city()),
                () -> Assertions.assertNotNull(userService.getUsers().get(0).companyDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(0).companyDTO().name(), COMPANY_LIST.get(0).getName()),
                () -> Assertions.assertNotNull(userService.getUsers().get(0).userSettingsFormDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(0).userSettingsFormDTO().emailNotification(), USER_SETTINGS_FORM_DTO_LIST.get(0).emailNotification()),
                () -> Assertions.assertEquals(userService.getUsers().get(1).name(), USER_FORM_DTO_LIST.get(1).name()),
                () -> Assertions.assertEquals(userService.getUsers().get(1).email(), USER_FORM_DTO_LIST.get(1).email()),
                () -> Assertions.assertNotNull(userService.getUsers().get(1).addressDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(1).addressDTO().city(), ADDRESS_FORM_DTO_LIST.get(1).city()),
                () -> Assertions.assertNotNull(userService.getUsers().get(1).companyDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(1).companyDTO().name(), COMPANY_LIST.get(1).getName()),
                () -> Assertions.assertNotNull(userService.getUsers().get(1).userSettingsFormDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(1).userSettingsFormDTO().emailNotification(), USER_SETTINGS_FORM_DTO_LIST.get(1).emailNotification()),
                () -> Assertions.assertEquals(userService.getUsers().get(2).name(), USER_FORM_DTO_LIST.get(2).name()),
                () -> Assertions.assertEquals(userService.getUsers().get(2).email(), USER_FORM_DTO_LIST.get(2).email()),
                () -> Assertions.assertNotNull(userService.getUsers().get(2).addressDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(2).addressDTO().city(), ADDRESS_FORM_DTO_LIST.get(2).city()),
                () -> Assertions.assertNotNull(userService.getUsers().get(2).companyDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(2).companyDTO().name(), COMPANY_LIST.get(2).getName()),
                () -> Assertions.assertNotNull(userService.getUsers().get(2).userSettingsFormDTO()),
                () -> Assertions.assertEquals(userService.getUsers().get(2).userSettingsFormDTO().emailNotification(), USER_SETTINGS_FORM_DTO_LIST.get(2).emailNotification())

        );
    }


    @Test
    void getUsersByCompanyIdShouldReturn() {
        // given
        UUID companyID = COMPANY_LIST.get(0).getId();
        List<User> users = List.of(
                new User(USER_FORM_DTO_LIST.get(0), "password1", new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0))
        );

        // when
        when(userRepository.findAllByCompanyId(any(UUID.class))).thenReturn(users);

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.getUsersByCompanyId(companyID)),
                () -> Assertions.assertEquals(userService.getUsersByCompanyId(companyID).size(), 1),
                () -> Assertions.assertEquals(userService.getUsersByCompanyId(companyID).get(0).name(), USER_FORM_DTO_LIST.get(0).name()),
                () -> Assertions.assertEquals(userService.getUsersByCompanyId(companyID).get(0).email(), USER_FORM_DTO_LIST.get(0).email()),
                () -> Assertions.assertNotNull(userService.getUsersByCompanyId(companyID).get(0).addressDTO()),
                () -> Assertions.assertEquals(userService.getUsersByCompanyId(companyID).get(0).addressDTO().city(), ADDRESS_FORM_DTO_LIST.get(0).city()),
                () -> Assertions.assertNotNull(userService.getUsersByCompanyId(companyID).get(0).companyDTO()),
                () -> Assertions.assertEquals(userService.getUsersByCompanyId(companyID).get(0).companyDTO().name(), COMPANY_LIST.get(0).getName()),
                () -> Assertions.assertNotNull(userService.getUsersByCompanyId(companyID).get(0).userSettingsFormDTO()),
                () -> Assertions.assertEquals(userService.getUsersByCompanyId(companyID).get(0).userSettingsFormDTO().emailNotification(), USER_SETTINGS_FORM_DTO_LIST.get(0).emailNotification())
        );
    }

    @Test
    void shouldCreateUser() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0));

        // when
        when(userRepository.save(any(User.class))).thenReturn(user);

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.create(USER_FORM_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0))),
                () -> Assertions.assertEquals(user.getEmail(), userService.create(USER_FORM_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0)).getEmail()),
                () -> Assertions.assertEquals(user.getAddress().getCity(), userService.create(USER_FORM_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0)).getAddress().getCity()),
                () -> Assertions.assertEquals(user.getCompany().getName(), userService.create(USER_FORM_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0)).getCompany().getName())
        );
    }

    @Test
    void createUserWithDifferentPasswordsShouldThrowBadRequestException() {
        // given
        UserFormDTO badUserFormDTO = new UserFormDTO("name1", "password1", "password2", "surname1", "email1@test.pl", "role1", "123123111", COMPANY_LIST.get(0).getId(), ADDRESS_FORM_DTO_LIST.get(0), null);

        // then
        Assertions.assertThrows(BadRequestException.class, () -> userService.create(badUserFormDTO, new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0)));
    }

    @Test
    void createAlreadyExistsUserShouldThrowBadRequestException() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0));

        // when
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        // then
        Assertions.assertThrows(BadRequestException.class, () -> userService.create(USER_FORM_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0)));
    }

    @Test
    void shouldUpdateLastActivityUser() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0));

        // when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // then
        Assertions.assertDoesNotThrow(() -> userService.updateLastActivityUser(user.getId()));
    }

    @Test
    void updateLastActivityUserShouldThrowNotFoundException() {
        // when
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> userService.updateLastActivityUser(UUID.randomUUID()));
    }

    @Test
    void shouldUpdateUser() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0));
        User newUser = new User(USER_FORM_DTO_LIST.get(1), passwordEncoder.encode(USER_FORM_DTO_LIST.get(1).password()), new Address(ADDRESS_FORM_DTO_LIST.get(1)), COMPANY_LIST.get(1));

        // when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // then
        Assertions.assertDoesNotThrow(() -> userService.update(user.getId(), USER_FORM_DTO_LIST.get(1), COMPANY_LIST.get(0)));
    }

    @Test
    void shouldUpdateUserWithNullAsNewAddress() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0));
        User newUser = new User(USER_FORM_DTO_LIST.get(1), passwordEncoder.encode(USER_FORM_DTO_LIST.get(1).password()), null, COMPANY_LIST.get(1));
        UserFormDTO userFormDTOWithoutAddress = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", COMPANY_LIST.get(0).getId(), null, null);

        // when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // then
        Assertions.assertDoesNotThrow(() -> userService.update(user.getId(), userFormDTOWithoutAddress, COMPANY_LIST.get(0)));
    }

    @Test
    void shouldUpdateUserWithNullAsCurrentAddress() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), null, COMPANY_LIST.get(0));
        User newUser = new User(USER_FORM_DTO_LIST.get(1), passwordEncoder.encode(USER_FORM_DTO_LIST.get(1).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(1));

        // when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // then
        Assertions.assertDoesNotThrow(() -> userService.update(user.getId(), USER_FORM_DTO_LIST.get(1), COMPANY_LIST.get(0)));
    }

    @Test
    void updateUserShouldThrowNotFoundException() {
        // when
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> userService.update(UUID.randomUUID(), USER_FORM_DTO_LIST.get(1), COMPANY_LIST.get(0)));
    }

    @Test
    void getUserByEmailShouldReturn() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0));
        UUID userID = user.getId();

        // when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.getUserById(userID)),
                () -> Assertions.assertEquals(userService.getUserById(userID).getName(), USER_FORM_DTO_LIST.get(0).name()),
                () -> Assertions.assertEquals(userService.getUserById(userID).getEmail(), USER_FORM_DTO_LIST.get(0).email()),
                () -> Assertions.assertNotNull(userService.getUserById(userID).getAddress()),
                () -> Assertions.assertEquals(userService.getUserById(userID).getAddress().getCity(), ADDRESS_FORM_DTO_LIST.get(0).city()),
                () -> Assertions.assertNotNull(userService.getUserById(userID).getCompany()),
                () -> Assertions.assertEquals(userService.getUserById(userID).getCompany().getName(), COMPANY_LIST.get(0).getName()),
                () -> Assertions.assertNotNull(userService.getUserById(userID).getUserSettings()),
                () -> Assertions.assertEquals(userService.getUserById(userID).getUserSettings().isEmailNotification(), USER_SETTINGS_FORM_DTO_LIST.get(0).emailNotification())
        );
    }

    @Test
    void getUserByEmailShouldThrowNotFoundException() {
        // when
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(UUID.randomUUID()));
    }

    @Test
    void getUserDataLoginByUserIDShouldReturn() {
        // given
        User user = new User(USER_FORM_DTO_LIST.get(0), passwordEncoder.encode(USER_FORM_DTO_LIST.get(0).password()), new Address(ADDRESS_FORM_DTO_LIST.get(0)), COMPANY_LIST.get(0));
        UUID userID = user.getId();

        // when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.getUserLoginDataByUserID(userID)),
                () -> Assertions.assertEquals(userService.getUserLoginDataByUserID(userID).id(), userID),
                () -> Assertions.assertEquals(userService.getUserLoginDataByUserID(userID).role(), user.getRole()),
                () -> Assertions.assertEquals(userService.getUserLoginDataByUserID(userID).enabled(), user.isEnabled()),
                () -> Assertions.assertEquals(userService.getUserLoginDataByUserID(userID).password(), user.getPassword()),
                () -> Assertions.assertEquals(userService.getUserLoginDataByUserID(userID).email(), user.getEmail())
        );
    }

    @Test
    void getUserDataLoginByUserIDShouldThrowNotFoundException() {
        // when
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserLoginDataByUserID(UUID.randomUUID()));
    }

    @Test
    public void changePasswordShouldChange() {
        // given
        Company company = new Company(COMPANY_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)));
        User user = new User(USER_FORM_DTO_LIST.get(0), "password", new Address(ADDRESS_FORM_DTO_LIST.get(0)), company);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("password", "newPass", "newPass");

        // when
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        // then
        Assertions.assertDoesNotThrow(() -> userService.changePassword(UUID.randomUUID(), changePasswordDTO));
    }

    @Test
    public void changePasswordForNotExistsUserShouldThrowBadRequestException() {
        // given
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("password", "newPass", "newPass");

        // when
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(BadRequestException.class, () -> userService.changePassword(UUID.randomUUID(), changePasswordDTO));
    }

    @Test
    public void changePasswordWithDifferentPasswordShouldThrowBadRequestException() {
        // given
        Company company = new Company(COMPANY_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)));
        User user = new User(USER_FORM_DTO_LIST.get(0), "password", new Address(ADDRESS_FORM_DTO_LIST.get(0)), company);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("password", "newPass", "newPass1");

        // when
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        // then
        Assertions.assertThrows(BadRequestException.class, () -> userService.changePassword(UUID.randomUUID(), changePasswordDTO));
    }

    @Test
    public void changePasswordWithBadPasswordShouldThrowBadRequestException() {
        // given
        Company company = new Company(COMPANY_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)));
        User user = new User(USER_FORM_DTO_LIST.get(0), "password", new Address(ADDRESS_FORM_DTO_LIST.get(0)), company);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("password1", "newPass", "newPass");

        // when
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

        // then
        Assertions.assertThrows(BadRequestException.class, () -> userService.changePassword(UUID.randomUUID(), changePasswordDTO));
    }

    @Test
    public void enableUserShouldEnable() {
        // given
        Company company = new Company(COMPANY_DTO_LIST.get(0), new Address(ADDRESS_FORM_DTO_LIST.get(0)));
        User user = new User(USER_FORM_DTO_LIST.get(0), "password", new Address(ADDRESS_FORM_DTO_LIST.get(0)), company);

        // when
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        // then
        Assertions.assertDoesNotThrow(() -> userService.enableUser(UUID.randomUUID()));
    }

    @Test
    public void enableUserShouldThrowNotFoundException() {
        // when
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class, () -> userService.enableUser(UUID.randomUUID()));
    }

}
