package usecase.authentication;

import core.entity.User;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.LocalUserDao;
import infrastructure.dto.RegistrationDto;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import usecase.authentication.registration.LocalRegistration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class RegistrationTest {
    private LocalRegistration localRegistration;
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        localRegistration = new LocalRegistration();
    }

    @Test
    public void register_With_Correct_Data() throws InvalidUserInformationException, InterruptedException {
        RegistrationDto registrationDto = new RegistrationDto(
                "correct@gmail.com",
                "user",
                "1"
        );

        boolean result = localRegistration.register(registrationDto);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void register_With_Incorrect_Email() throws InvalidUserInformationException, InterruptedException {
        RegistrationDto registrationDto = new RegistrationDto(
                "incorrectgmail.com",
                "user",
                "1"
        );
        Assert.assertThrows(InvalidUserInformationException.class, () -> localRegistration.register(registrationDto));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        String exceptedConsoleOutput = "Некорректный формат электронной почты!";
        String actualConsoleOutput = outputStream.toString().trim();
        Assertions.assertThat(actualConsoleOutput).isEqualTo(exceptedConsoleOutput);

        System.setOut(originalOut);
    }

    @Test
    public void register_With_Incorrect_Password_And_Username() {
        RegistrationDto registrationDto = new RegistrationDto(
                "correct@gmail.com",
                "",
                ""
        );

        Assert.assertThrows(InvalidUserInformationException.class, () -> localRegistration.register(registrationDto));
    }

    @Test
    public void register_With_Blank_Data() {
        RegistrationDto registrationDto = new RegistrationDto(
                "",
                "",
                ""
        );

        Assert.assertThrows(InvalidUserInformationException.class, () -> localRegistration.register(registrationDto));
    }

    @Test
    public void account_Already_Exists() throws InvalidUserInformationException, InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        String existingEmail = "emailAlreadyExists@gmail.com";
        RegistrationDto registrationDto = new RegistrationDto(existingEmail, "username", "password");
        new LocalUserDao().add(User.builder().email(existingEmail).build());

        boolean result = localRegistration.register(registrationDto);
        Assert.assertFalse(result);

        String exceptedConsoleOutput = "Учётная запись с указанной электронной почтой уже зарегистрирована!";
        String actualConsoleOutput = outputStream.toString().trim();

        Assertions.assertThat(actualConsoleOutput).isEqualTo(exceptedConsoleOutput);
        System.setOut(originalOut);
    }
}