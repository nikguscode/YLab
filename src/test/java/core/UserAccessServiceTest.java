package core;

import core.entity.User;
import core.enumiration.Role;
import core.exceptions.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dao.user.JdbcUserDao;
import infrastructure.dao.user.UserDao;
import infrastructure.dto.RegistrationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import usecase.authentication.registration.JdbcRegistration;
import usecase.authentication.registration.Registration;

import java.time.LocalDateTime;

public class UserAccessServiceTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private UserDao userDao;
    private Registration registration;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
        new LiquibaseMigration(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        ).migrateDatabase();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    public void setUp() {
        DatabaseUtils databaseUtils = new DatabaseUtils(
                "org.postgresql.Driver",
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        this.registration = new JdbcRegistration(new JdbcUserDao(databaseUtils));
        this.userDao = new JdbcUserDao(databaseUtils);
    }

    @AfterEach
    public void afterEach() {
    }

    @Test
    @DisplayName("UNDEFINED")
    public void test_Undefined() throws InvalidUserInformationException {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email("undefined@gmail.com")
                .username("undefined")
                .password("undefined")
                .build();
        registration.isSuccess(registrationDto);
        User user = userDao.get("undefined@gmail.com");
        user.setRole(Role.UNDEFINED);

        boolean result = UserAccessService.hasAccess(userDao, user);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("BLOCKED USER")
    public void test_Blocked_User() throws InvalidUserInformationException {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email("blocked@gmail.com")
                .username("blocked")
                .password("blocked")
                .build();
        registration.isSuccess(registrationDto);
        User user = userDao.get("blocked@gmail.com");
        user.setRole(Role.BLOCKED);

        boolean result = UserAccessService.hasAccess(userDao, user);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("STANDARD USER")
    public void test_Standard_User() throws InvalidUserInformationException {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email("user@gmail.com")
                .username("user")
                .password("user")
                .build();
        registration.isSuccess(registrationDto);
        User user = userDao.get("user@gmail.com");
        user.setRole(Role.USER);

        boolean result = UserAccessService.hasAccess(userDao, user);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("USER NOT FOUND IN DATABASE")
    public void test_User_Not_Found_In_Database() throws InvalidUserInformationException {
        User user = User.builder()
                .email("not_found_in_database@gmail.com")
                .username("not_found_in_database")
                .password("not_found_in_database")
                .role(Role.USER)
                .registrationDate(LocalDateTime.now())
                .authorizationDate(LocalDateTime.now())
                .build();
        boolean result = UserAccessService.hasAccess(userDao, user);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("USER IS NOT AUTHORIZED")
    public void test_User_Is_Not_Authorized() throws InvalidUserInformationException {
        User user = User.builder()
                .email("not_authorized@gmail.com")
                .username("not_authorized")
                .password("not_authorized")
                .role(Role.USER)
                .isAuthorized(false)
                .registrationDate(LocalDateTime.now())
                .authorizationDate(LocalDateTime.now())
                .build();
        boolean result = UserAccessService.hasAccess(userDao, user);
        Assertions.assertThat(result).isFalse();
    }
}