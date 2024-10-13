package adapters.in;

import adapters.console.Constants;
import infrastructure.dto.LoginDto;

import java.util.Scanner;

public class LoginInput implements ConsoleInput<LoginDto> {
    @Override
    public LoginDto input(Scanner scanner) {
        LoginDto.Builder loginBuilder = LoginDto.builder();
        System.out.println(Constants.AUTHENTICATION_MENU);

        System.out.print("Введите почту: ");
        loginBuilder.email(scanner.nextLine());

        System.out.print("Введите пароль: ");
        loginBuilder.password(scanner.nextLine());

        return loginBuilder.build();
    }
}