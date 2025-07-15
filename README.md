# Users management

The project is used in other projects to manage user experience. It creates appropriate database tables representing
users, notification settings, addresses and companies in the relation database - PostgreSQL.

## Integration

Add dependency to `pom.xml` file as below:

```
<dependency>
  <groupId>users.management</groupId>
  <artifactId>UsersManagement</artifactId>
  <version>${users-management-version}</version>
</dependency>
```

NOTE! Remember to start flaway files in your project with 'V1...'.