package users.management.service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import users.management.exception.EmailException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailSenderImplTest {

    @Mock
    JavaMailSender javaMailSender;

    EmailSenderImpl emailSender;

    @BeforeEach
    void setUp() {
        emailSender = new EmailSenderImpl(javaMailSender, "email");
    }

    @Test
    public void sendEmailShouldNotThrow() {
        // given
        MimeMessage message = new MimeMessage((Session) null);

        // when
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        // then
        Assertions.assertDoesNotThrow(() -> emailSender.sendEmail("to", "title", "content"));
    }

    @Test
    public void sendEmailShouldThrowEmailExceptionByMimeMessageHelper() throws MessagingException {
        // given
        MimeMessage mimeMessageMock = mock(MimeMessage.class);

        // when
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageMock);
        doThrow(new MessagingException("Invalid")).when(mimeMessageMock).setContent(any());

        // then
        assertThrows(EmailException.class, () -> emailSender.sendEmail("to@example.com", "title", "content"));
    }

    @Test
    void shouldThrowEmailException_whenMailExceptionOccurs() {
        // when
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        doThrow(new MailSendException("Cannot send")).when(javaMailSender).send(any(MimeMessage.class));

        // then
        assertThrows(EmailException.class, () -> emailSender.sendEmail("to@example.com", "title", "content"));
    }

}
