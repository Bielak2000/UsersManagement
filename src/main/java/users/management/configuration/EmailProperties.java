package users.management.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("users.management.email")
public class EmailProperties {

    private String redirectActivateAccountUrl;
    private String redirectResetPasswordUrl;
    private String tokenValidityHours;
    private String systemName;

}
