package adapters.in;

import adapters.console.Constants;
import infrastructure.dto.LoginDto;

import java.util.Scanner;

public class LoginInput implements ConsoleInput<LoginDto> {
    @Override
    public LoginDto input(Scanner scanner) {
        System.out.println(Constants.AUTHENTICATION_MENU);

        System.out.print("Введите почту: ");
        String email = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        return new LoginDto(email, password);
    }
}