package users.management.mock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import users.management.exception.EmailException;
import users.management.service.EmailSender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Slf4j
@AllArgsConstructor
public class EmailSenderMock implements EmailSender {

    private final String mailSender;

    @Override
    public void sendEmail(@Email(message = "Email recipient address was invalid") String mailRecipient,
                          @NotBlank(message = "Email title was blank") String title,
                          @NotBlank(message = "Email content was blank") String content) throws EmailException {
        log.info("Mock-sending email `{}` from `{}` to `{}`", title, mailSender, mailRecipient);
    }

}
