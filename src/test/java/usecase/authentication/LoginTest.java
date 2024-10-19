package usecase.authentication;

import core.entity.User;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.LocalUserDao;
import infrastructure.dao.user.UserDao;
import infrastructure.dto.LoginDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usecase.authentication.login.LocalLogin;
import usecase.authentication.login.Login;

public class LoginTest {
    private final Login login;

    public LoginTest() throws InvalidUserInformationException {
        login = new LocalLogin();
        UserDao userDao = new LocalUserDao();

        userDao.add(User.builder()
                .email("correct@gmail.com")
                .username("some username")
                .password("1")
                .build()
        );
    }

    @Test
    @DisplayName("Login With Correct Data")
    public void login_With_Correct_Data() {
        boolean result = login.isSuccess(
                LoginDto.builder()
                .email("correct@gmail.com")
                .password("1")
                .build()
        );
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Login If Email Doesn't Exists")
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
                        .email("correct@gmail.com")
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