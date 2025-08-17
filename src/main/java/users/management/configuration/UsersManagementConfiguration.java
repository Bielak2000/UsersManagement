package users.management.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import users.management.UsersManagementFacade;
import users.management.mock.EmailSenderMock;
import users.management.repository.AddressRepository;
import users.management.repository.CompanyRepository;
import users.management.repository.UserRepository;
import users.management.repository.UserSettingsRepository;
import users.management.repository.VerificationTokenRepository;
import users.management.service.AddressService;
import users.management.service.CompanyService;
import users.management.service.EmailSender;
import users.management.service.EmailSenderImpl;
import users.management.service.EmailSenderService;
import users.management.service.UserService;
import users.management.service.UserSettingsService;
import users.management.service.VerificationTokenService;

@Configuration
@ComponentScan(basePackages = "users.management")
@EnableConfigurationProperties(EmailProperties.class)
public class UsersManagementConfiguration {

    @Bean
    public AddressService addressService(AddressRepository addressRepository) {
        return new AddressService(addressRepository);
    }

    @Bean
    public CompanyService companyService(CompanyRepository companyRepository) {
        return new CompanyService(companyRepository);
    }

    @Bean
    public UserService userService(@Value("${spring.profiles.active}") String activeProfile, UserRepository userRepository) {
        return new UserService(activeProfile, new BCryptPasswordEncoder(), userRepository);
    }

    @Bean
    public UserSettingsService userSettingsService(UserSettingsRepository userSettingsRepository) {
        return new UserSettingsService(userSettingsRepository);
    }

    @Bean
    public VerificationTokenService verificationTokenService(@Value("${users.management.email.token.validity.hours}") int tokenValidityHours,
                                                             VerificationTokenRepository verificationTokenRepository) {
        return new VerificationTokenService(tokenValidityHours, verificationTokenRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringTemplateEngine templateEngine() {
        return new SpringTemplateEngine();
    }

    @Profile("mock-mail")
    @Bean
    public EmailSender mockEmailSender() {
        return new EmailSenderMock("MockMailSender");
    }

    @Profile("!mock-mail")
    @Bean
    public EmailSender emailSenderImpl(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String mailUsername) {
        return new EmailSenderImpl(javaMailSender, mailUsername);
    }

    @Bean
    public EmailSenderService emailSenderService(EmailSender emailSender, TemplateEngine templateEngine, EmailProperties emailProperties) {
        return new EmailSenderService(emailProperties, emailSender, templateEngine);
    }

    @Bean
    public UsersManagementFacade usersManagementFacade(AddressService addressService, CompanyService companyService, UserService userService, UserSettingsService userSettingsService, VerificationTokenService verificationTokenService, EmailSenderService emailSenderService) {
        return new UsersManagementFacade(companyService, addressService, userService, userSettingsService, verificationTokenService, emailSenderService);
    }

}
