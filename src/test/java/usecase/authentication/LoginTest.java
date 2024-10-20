package usecase.authentication;

import core.entity.User;
import core.enumiration.Role;
import core.exceptions.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dao.user.JdbcUserDao;
import infrastructure.dao.user.UserDao;
import infrastructure.dto.LoginDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import usecase.authentication.login.JdbcLogin;
import usecase.authentication.login.Login;

import java.time.LocalDateTime;

public class LoginTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private Login login;
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
    public void setUp() throws InvalidUserInformationException {
        DatabaseUtils databaseUtils = new DatabaseUtils(
                "org.postgresql.Driver",
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        this.login = new JdbcLogin(new JdbcUserDao(databaseUtils));
        this.userDao = new JdbcUserDao(databaseUtils);
        this.user = User.builder()
                .email("test@gmail.com")
                .username("testuser")
                .password("root")
                .role(Role.ADMINISTRATOR)
                .registrationDate(LocalDateTime.now())
                .authorizationDate(LocalDateTime.now())
                .build();

        userDao.add(user);
    }

    @AfterEach
    public void afterEach() {
        userDao.delete(user);
        user = null;
    }

    @Test
    @DisplayName("Login With Correct Data")
    public void login_With_Correct_Data() {
        boolean result = login.isSuccess(
                LoginDto.builder()
                        .email("test@gmail.com")
                        .password("root")
                        .build()
        );
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Login If Email Doesn't Exist")
    public void login_If_Email_Not_Exists() {
        boolean result = login.isSuccess(
                LoginDto.builder()
                        .email("notExistsEmail@gmail.com")
                        .password("1")
                        .build()
        );
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Login With Incorrect Password")
    public void login_With_Incorrect_Password() {
        boolean result = login.isSuccess(
                LoginDto.builder()
                        .email("admin@gmail.com")
                        .password("incorrectPassword")
                        .build()
        );
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Login With Blank Data")
    public void login_With_Blank_Data() {
        boolean result = login.isSuccess(
                LoginDto.builder()
                        .email("")
                        .password("")
                        .build()
        );
        Assertions.assertThat(result).isFalse();
    }
}