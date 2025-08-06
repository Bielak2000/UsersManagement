package users.management.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import users.management.exception.EmailException;

@RequiredArgsConstructor
@Slf4j
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final String mailSender;

    @Override
    public void sendEmail(String to, String title, String content) throws EmailException {
        log.info("Sending email '{}' to {}", title, to);
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(mailSender);
            helper.setSubject(title);
            helper.setText(content, true);
            javaMailSender.send(mail);
            log.info("Email '{}' sent to {}", title, to);
        } catch (MessagingException e) {
            throw new EmailException(String.format("Cannot create email: %s", e.getMessage()), e);
        } catch (MailException e) {
            throw new EmailException(String.format("Cannot send email: %s", e.getMessage()), e);
        }
    }

}
