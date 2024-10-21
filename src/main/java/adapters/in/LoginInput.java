package adapters.in;

import adapters.console.Constants;
import infrastructure.dto.HabitDto;
import infrastructure.dto.LoginDto;

import java.util.Scanner;

/**
 * Данный класс предназначен для заполнения {@link LoginDto} пользовательским вводом и последующей передачей в
 * {@link usecase.authentication.login.Login Login}
 */
public class LoginInput implements ConsoleInput<LoginDto> {
    /**
     * Метод, используемый для обработки пользовательского ввода при входе в учётную запись
     * @param scanner экземпляр сканера
     * @return DTO класс, содержащий пользовательский ввод
     */
    @Override
    public LoginDto input(Scanner scanner) {
        LoginDto.LoginDtoBuilder loginBuilder = LoginDto.builder();
        System.out.println(Constants.AUTHENTICATION_MENU);

        System.out.print("Введите почту: ");
        loginBuilder.email(scanner.nextLine());

        System.out.print("Введите пароль: ");
        loginBuilder.password(scanner.nextLine());

        return loginBuilder.build();
    }
}