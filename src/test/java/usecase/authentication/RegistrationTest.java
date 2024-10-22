package usecase.authentication;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class RegistrationTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private Registration registration;
    private UserDao userDao;
    private User user;

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
        if (user != null) {
            userDao.delete(user);
            user = null;
        }
    }

    @Test
    @DisplayName("Register With Correct Data")
    public void register_With_Correct_Data() throws InvalidUserInformationException {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email("test@gmail.com")
                .username("test")
                .password("test")
                .build();

        boolean result = registration.isSuccess(registrationDto);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Register With Incorrect Email")
    public void register_With_Incorrect_Email() {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email("incorrectgmail.com")
                .username("user")
                .password("1")
                .build();
        assertThatThrownBy(() -> registration.isSuccess(registrationDto))
                .isInstanceOf(InvalidUserInformationException.class);
    }

    @Test
    @DisplayName("Register With Blank Data")
    public void register_With_Blank_Data() {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email("")
                .username("")
                .password("")
                .build();
        assertThatThrownBy(() -> registration.isSuccess(registrationDto))
                .isInstanceOf(InvalidUserInformationException.class);
    }

    @Test
    @DisplayName("Account Already Exists")
    public void account_Already_Exists() throws InvalidUserInformationException {
        String existingEmail = "emailAlreadyExists@gmail.com";
        RegistrationDto registrationDto = RegistrationDto.builder()
                .email(existingEmail)
                .username("username")
                .password("password")
                .build();

        user = User.builder()
                .email(existingEmail)
                .username("username")
                .password("password")
                .role(Role.USER)
                .registrationDate(LocalDateTime.now())
                .authorizationDate(LocalDateTime.now())
                .build();

        userDao.add(user);

        boolean result = registration.isSuccess(registrationDto);
        Assertions.assertThat(result).isFalse();
    }
}