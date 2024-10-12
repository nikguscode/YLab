package adapters.in;

import adapters.console.Constants;
import infrastructure.dto.RegistrationDto;

import java.util.Scanner;

public class RegistrationInput implements ConsoleInput<RegistrationDto> {
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