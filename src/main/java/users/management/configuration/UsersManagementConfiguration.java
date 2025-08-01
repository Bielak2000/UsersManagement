package users.management.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import users.management.UsersManagementFacade;
import users.management.repository.AddressRepository;
import users.management.repository.CompanyRepository;
import users.management.repository.UserRepository;
import users.management.repository.UserSettingsRepository;
import users.management.repository.VerificationTokenRepository;
import users.management.service.AddressService;
import users.management.service.CompanyService;
import users.management.service.UserService;
import users.management.service.UserSettingsService;
import users.management.service.VerificationTokenService;

@Configuration
@ComponentScan(basePackages = "users.management")
@EntityScan(basePackages = "users.management.entity")
@EnableJpaRepositories("users.management.repository")
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
    public UserService userService(UserRepository userRepository) {
        return new UserService(new BCryptPasswordEncoder(), userRepository);
    }

    @Bean
    public UserSettingsService userSettingsService(UserSettingsRepository userSettingsRepository) {
        return new UserSettingsService(userSettingsRepository);
    }

    @Bean
    public VerificationTokenService verificationTokenService(@Value("${users.management.token.validity.hours}") int tokenValidityHours,
                                                             VerificationTokenRepository verificationTokenRepository) {
        return new VerificationTokenService(tokenValidityHours, verificationTokenRepository);
    }

    @Bean
    public UsersManagementFacade usersManagementFacade(AddressService addressService, CompanyService companyService, UserService userService, UserSettingsService userSettingsService, VerificationTokenService verificationTokenService) {
        return new UsersManagementFacade(companyService, addressService, userService, userSettingsService, verificationTokenService);
    }

}
