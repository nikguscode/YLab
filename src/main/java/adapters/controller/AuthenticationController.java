package adapters.controller;

import adapters.console.Constants;
import adapters.in.ConsoleInput;
import adapters.in.LoginInput;
import adapters.in.RegistrationInput;
import core.entity.User;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.user.UserDao;
import infrastructure.dto.LoginDto;
import infrastructure.dto.RegistrationDto;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidUserInformationException;
import lombok.RequiredArgsConstructor;
import usecase.authentication.login.Login;
import usecase.authentication.registration.Registration;

import java.util.Scanner;

/**
 * <p>Контроллер, используемый при аутентификации пользователя</p>
 * <p>Вызывает следующие сервисы при своей работе:
 * <ul>
 *     <li>{@link Login}</li>
 *     <li>{@link Registration}</li>
 *     <li>{@link UserDao}</li>
 * </ul></p>
 */
@RequiredArgsConstructor
public class AuthenticationController {
    private final MainController mainController;
    private final Login login;
    private final Registration registration;
    private final UserDao userDao;
    private final HabitDao habitDao;

    public void handle(Scanner scanner) throws InvalidUserInformationException, InvalidFrequencyConversionException {
        while (true) {
            System.out.print(Constants.START_MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1. Войти в учётную запись", "Войти в учётную запись", "1.":
                    ConsoleInput<LoginDto> loginInput = new LoginInput();
                    LoginDto loginDto = loginInput.input(scanner);

                    if (login.isSuccess(loginDto)) {
                        System.out.println("Пользователь авторизован...");

                        mainController.handle(scanner, userDao.get(loginDto.getEmail()));
                    }
                    break;
                case "2", "2. Регистрация учётной записи", "Регистрация учётной записи", "2.":
                    ConsoleInput<RegistrationDto> registrationInput = new RegistrationInput();
                    RegistrationDto registrationDto = registrationInput.input(scanner);

                    if (registration.isSuccess(registrationDto)) {
                        System.out.println("Пользователь зарегистрирован...");
                        mainController.handle(scanner, userDao.get(registrationDto.getEmail()));
                    }
                    break;
                default:
                    System.out.println("Указана некорректная опция!");
            }
        }
    }
}