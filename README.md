# Users management

The project is used in other projects to manage user experience. It creates appropriate database tables representing
users, notification settings, addresses and companies in the relation database - PostgreSQL.

## Integration

### Dependency
Add dependency to `pom.xml` file as below:

```
<dependency>
  <groupId>users.management</groupId>
  <artifactId>UsersManagement</artifactId>
  <version>${users-management-version}</version>
</dependency>
```

**NOTE!**
Remember to start flyway files in your project with 'V1...' and add below annotation above main spring boot application
class:

```
@Import({users.management.configuration.UsersManagementConfiguration.class})
```

### Properties

Additionally, you should set the following properties in the application.properties file:

* `users.management.email.token.validity.hours` - validity period (in hours) of tokens for account verification and password reset,
* `spring.mail.test-connection` — checks the connection to the mail server during application startup
* `spring.mail.host` — SMTP server hostname
* `spring.mail.port` — SMTP server port
* `spring.mail.username` — username for logging in to the mail server
* `spring.mail.password` — password for logging in to the mail server
* `spring.mail.properties.mail.smtp.auth` — enables SMTP authentication (`true/false`)
* `spring.mail.properties.mail.smtp.starttls.enable` — enables STARTTLS (connection encryption)
* `spring.mail.properties.mail.smtp.ssl.enable` — enables SSL encryption for SMTP
* `spring.mail.properties.mail.smtp.starttls.required` — requires STARTTLS if the server supports it  

### Possible profiles

If you want to set a profile you should set properties `spring.profiles.active`.

Possible profiles:
* `mock-email` - mocks sending verification account and user reset password email addresses.