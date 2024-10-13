package usecase.authentication;

import core.entity.User;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.LocalUserDao;
import infrastructure.dao.user.UserDao;
import infrastructure.dto.LoginDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import usecase.authentication.login.LocalLogin;
import usecase.authentication.login.Login;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LoginTest {
    private final Login login;
    private final PrintStream originalOut = System.out;

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
    public void login_With_Correct_Data() throws InvalidUserInformationException, InterruptedException {
        boolean result = login.isSuccess(
                LoginDto.builder()
                .email("correct@gmail.com")
                .password("1")
                .build()
        );
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void login_If_Email_Not_Exists() throws InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        boolean result = login.isSuccess(
                LoginDto.builder()
                        .email("notExistsEmail@gmail.com")
                        .password("1")
                        .build()
        );
        Assertions.assertThat(result).isFalse();

        String exceptedConsoleOutput = "Указанный пользователь не найден! Проверьте корректность вводимых данных";
        String actualConsoleOutput = outputStream.toString().trim();
        Assertions.assertThat(actualConsoleOutput).isEqualTo(exceptedConsoleOutput);

        System.setOut(originalOut);
    }

    @Test
    public void login_With_Incorrect_Password() throws InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        boolean result = login.isSuccess(
                LoginDto.builder()
                        .email("correct@gmail.com")
                        .password("incorrectPassword")
                        .build()
        );
        Assertions.assertThat(result).isFalse();

        String exceptedConsoleOutput = "Проверьте корректность вводимой почты и пароля!";
        String actualConsoleOutput = outputStream.toString().trim();
        Assertions.assertThat(actualConsoleOutput).isEqualTo(exceptedConsoleOutput);

        System.setOut(originalOut);
    }

    @Test
    public void login_With_Blank_Data() throws InterruptedException {
        boolean result = login.isSuccess(
                LoginDto.builder()
                        .email("")
                        .password("")
                        .build()
        );
        Assertions.assertThat(result).isFalse();
    }
}
