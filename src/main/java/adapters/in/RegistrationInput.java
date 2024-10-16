package adapters.in;

import adapters.console.Constants;
import infrastructure.dto.LoginDto;
import infrastructure.dto.RegistrationDto;

import java.util.Scanner;


/**
 * Данный класс предназначен для заполнения {@link RegistrationDto} пользовательским вводом и последующей передачей в
 * {@link usecase.authentication.registration.Registration Registration}
 */
public class RegistrationInput implements ConsoleInput<RegistrationDto> {
    /**
     * Метод, используемый для обработки пользовательского ввода при регистрации учётной записи
     * @param scanner экземпляр сканера
     * @return DTO класс, содержащий пользовательский ввод
     */
    @Override
    public RegistrationDto input(Scanner scanner) {
        System.out.println(Constants.REGISTRATION_MENU);

        System.out.print("Введите почту: ");
        String email = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        System.out.print("Введите Ваше имя: ");
        String firstName = scanner.nextLine();

        return new RegistrationDto(email, firstName, password);
    }
}