package users.management.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import users.management.configuration.EmailProperties;
import users.management.exception.EmailException;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class EmailSenderService {

    private final EmailProperties emailProperties;
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;

    @Async("mail-task-thread")
    public void verificationAccount(String email, String username, String token, UUID userId) {
        Context context = new Context();
        String template = "mails/ActivationAccountTemplate";
        context.setVariable("userName", username);
        context.setVariable("systemName", emailProperties.getSystemName());
        context.setVariable("tokenValidityHours", emailProperties.getTokenValidityHours());
        String redirectUrl = String.format("%s?userId=%s&token=%s", emailProperties.getRedirectActivateAccountUrl(), userId, token);
        context.setVariable("redirectUrl", redirectUrl);

        String body = templateEngine.process(template, context);
        String title = String.format("Aktywuj swoje konto w systemie %s", emailProperties.getSystemName());
        try {
            emailSender.sendEmail(email, title, body);
        } catch (EmailException e) {
            log.error("Error while sending verification account email: {}", e.getMessage());
        }
    }

    @Async("mail-task-thread")
    public void resetPassword(String email, String username, String token, UUID userId) {
        Context context = new Context();
        String template = "mails/ResetPasswordTemplate";
        context.setVariable("userName", username);
        context.setVariable("systemName", emailProperties.getSystemName());
        context.setVariable("tokenValidityHours", emailProperties.getTokenValidityHours());
        String redirectUrl = String.format("%s?userId=%s&token=%s", emailProperties.getRedirectActivateAccountUrl(), userId, token);
        context.setVariable("redirectUrl", redirectUrl);

        String body = templateEngine.process(template, context);
        String title = String.format("Zresetuj swoje hasło w systemie %s", emailProperties.getSystemName());
        try {
            emailSender.sendEmail(email, title, body);
        } catch (EmailException e) {
            log.error("Error while sending reset password email: {}", e.getMessage());
        }
    }

}
