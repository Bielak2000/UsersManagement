package users.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.management.dto.AddressFormDTO;
import users.management.dto.CompanyFormDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserSettingsFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.entity.User;
import users.management.repository.UserSettingsRepository;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserSettingsServiceTest {

    @Mock
    UserSettingsRepository userSettingsRepository;

    @InjectMocks
    UserSettingsService userSettingsService;

    @Test
    void updateUserSettingsShouldNotThrow() {
        // given
        AddressFormDTO addressFormDTO = new AddressFormDTO(UUID.randomUUID(), "town1", "postalCode1", "test1@test.pl", null, "city1", "street1");
        CompanyFormDTO companyFormDTO = new CompanyFormDTO("name1", "tax1", "123123111", "email1@test.pl", addressFormDTO);
        UserSettingsFormDTO userSettingsFormDTO = new UserSettingsFormDTO(false, false, false);
        UserFormDTO userFormDTO = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", UUID.randomUUID(), addressFormDTO, userSettingsFormDTO);
        User user = new User(userFormDTO, "password1", new Address(addressFormDTO), new Company(companyFormDTO, new Address(addressFormDTO)), true);
        UserSettingsFormDTO newUserSettingsFormDTO = new UserSettingsFormDTO(false, true, false);

        // then
        Assertions.assertDoesNotThrow(() -> userSettingsService.updateUserSettings(user, newUserSettingsFormDTO));
    }

}
