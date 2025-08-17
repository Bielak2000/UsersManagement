package users.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.management.dto.AddressFormDTO;
import users.management.dto.CompanyFormDTO;
import users.management.dto.UserFormDTO;
import users.management.dto.UserSettingsFormDTO;
import users.management.entity.Address;
import users.management.entity.Company;
import users.management.entity.User;
import users.management.entity.VerificationToken;
import users.management.exception.NotFoundException;
import users.management.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceTest {

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    VerificationTokenService verificationTokenService;

    private static final UserSettingsFormDTO USER_SETTINGS_FORM_DTO = new UserSettingsFormDTO(false, true, false);
    private static final AddressFormDTO ADDRESS_FORM_DTO = new AddressFormDTO(UUID.randomUUID(), "town", "postalCode", "houseNumber", "apartmentNumber", "city", "street");
    private static final CompanyFormDTO COMPANY_FORM_DTO = new CompanyFormDTO("name", "tax", "123123123", "email", ADDRESS_FORM_DTO);
    private static final UserFormDTO USER_FORM_DTO = new UserFormDTO("name1", "password1", "password1", "surname1", "email1@test.pl", "role1", "123123111", UUID.randomUUID(), ADDRESS_FORM_DTO, USER_SETTINGS_FORM_DTO);
    private static final User USER = new User(USER_FORM_DTO, "password", new Address(ADDRESS_FORM_DTO), new Company(COMPANY_FORM_DTO, new Address(ADDRESS_FORM_DTO)), true);
    private static final VerificationToken VERIFICATION_TOKEN = new VerificationToken("TOKEN", LocalDateTime.now().plusHours(30), USER);

    @BeforeEach
    void setUp() {
        verificationTokenService = new VerificationTokenService(24, verificationTokenRepository);
    }

    @Test
    void shouldCreateVerificationToken() {
        // when
        when(verificationTokenRepository.save(any(VerificationToken.class))).thenReturn(VERIFICATION_TOKEN);

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> verificationTokenService.create(USER)),
                () -> Assertions.assertNotNull(VERIFICATION_TOKEN.getToken())
        );
    }

    @Test
    void isValidTokenShouldReturnTrue() {
        // when
        when(verificationTokenRepository.findByTokenAndUserId(any(), any())).thenReturn(Optional.of(VERIFICATION_TOKEN));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> verificationTokenService.isValidToken(VERIFICATION_TOKEN.getToken(), VERIFICATION_TOKEN.getUser().getId())),
                () -> Assertions.assertTrue(verificationTokenService.isValidToken(VERIFICATION_TOKEN.getToken(), VERIFICATION_TOKEN.getUser().getId()))
        );
    }

    @Test
    void isValidTokenShouldReturnFalse() {
        // given
        VerificationToken notValidVerificationToken = new VerificationToken("TOKEN", LocalDateTime.now().minusHours(1), USER);

        // when
        when(verificationTokenRepository.findByTokenAndUserId(any(), any())).thenReturn(Optional.of(notValidVerificationToken));

        // then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> verificationTokenService.isValidToken(VERIFICATION_TOKEN.getToken(), VERIFICATION_TOKEN.getUser().getId())),
                () -> Assertions.assertFalse(verificationTokenService.isValidToken(VERIFICATION_TOKEN.getToken(), VERIFICATION_TOKEN.getUser().getId()))
        );
    }

    @Test
    void isValidTokenShouldThrowNotFoundException() {
        // when
        when(verificationTokenRepository.findByTokenAndUserId(any(), any())).thenReturn(Optional.empty());

        // then
        Assertions.assertAll(
                () -> Assertions.assertThrows(NotFoundException.class, () -> verificationTokenService.isValidToken(VERIFICATION_TOKEN.getToken(), VERIFICATION_TOKEN.getUser().getId()))
        );
    }

}
