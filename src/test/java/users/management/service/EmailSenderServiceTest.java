package users.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import users.management.configuration.EmailProperties;
import users.management.exception.EmailException;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {

    @Mock
    private EmailProperties emailProperties;

    @Mock
    private EmailSender emailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailSenderService emailSenderService;

    private final static UUID USER_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        when(emailProperties.getSystemName()).thenReturn("MySystem");
        when(emailProperties.getTokenValidityHours()).thenReturn(String.valueOf(24));
        when(emailProperties.getRedirectActivateAccountUrl()).thenReturn("http://test/activate");
        when(templateEngine.process(anyString(), ArgumentMatchers.any(Context.class))).thenReturn("<html>body</html>");
    }

    @Test
    void verificationAccountShouldSendEmail() {
        // then
        Assertions.assertDoesNotThrow(() -> emailSenderService.verificationAccount("user@test.com", "John", "token123", USER_ID));
    }

    @Test
    void verificationAccount_shouldCatchEmailException() throws EmailException {
        // when
        doThrow(new EmailException("fail", new Throwable())).when(emailSender).sendEmail(anyString(), anyString(), anyString());

        // then
        Assertions.assertDoesNotThrow(() -> emailSenderService.verificationAccount("user@test.com", "John", "token123", USER_ID));
    }

    // ----------- resetPassword -----------

    @Test
    void resetPassword_shouldSendEmail_whenNoException() {
        // then
        Assertions.assertDoesNotThrow(() -> emailSenderService.resetPassword("user@test.com", "John", "token123", USER_ID));
    }

    @Test
    void resetPassword_shouldCatchEmailException() throws EmailException {
        // when
        doThrow(new EmailException("fail", new Throwable())).when(emailSender).sendEmail(anyString(), anyString(), anyString());

        // then
        Assertions.assertDoesNotThrow(() -> emailSenderService.resetPassword("user@test.com", "John", "token123", USER_ID));
    }

}
