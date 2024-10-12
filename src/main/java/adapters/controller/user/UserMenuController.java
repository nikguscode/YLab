package adapters.controller.user;

import adapters.console.Constants;
import adapters.out.UserInformationOutput;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.LocalUserDao;
import core.entity.User;

import java.util.Scanner;

public class UserMenuController {
    public void handle(Scanner scanner, User user) throws InterruptedException, InvalidUserInformationException {
        System.out.print(Constants.USER_SETTINGS);
        String input = scanner.nextLine();

        switch (input) {
            case "1", "1.", "Изменить имя", "1. Изменить имя":
                System.out.print("Укажите новое имя: ");
                user.setUsername(scanner.nextLine());
                System.out.println("Имя изменено");
                Thread.sleep(500);
                break;
            case "2", "2.", "Изменить пароль", "2. Изменить пароль":
                System.out.print("Укажите новый пароль: ");
                user.setPassword(scanner.nextLine());
                System.out.println("Пароль изменен");
                Thread.sleep(500);
                break;
            case "3", "3.", "Изменить электронную почту", "3. Изменить электронную почту":
                System.out.print("Укажите новую электронную почту: ");
                user.setEmail(scanner.nextLine());
                System.out.println("Электронная почта изменена");
                Thread.sleep(500);
                break;
            case "4", "4.", "Информация об учётной записи", "4. Информация об учётной записи":
                new UserInformationOutput().output(user);
                System.out.print("Выберите опцию: ");
                scanner.nextLine();
                break;
            case "5", "5.", "Удалить учётную запись", "5. Удалить учётную запись":
                new LocalUserDao().delete(user);
                System.out.println("Удаление учётной записи...");
                Thread.sleep(1000);
                return;
            case "0", "0.", "Вернуться в главное меню", "0. Вернуться в главное меню":
                return;
            default:
                System.out.println("Указана некорректная опция!");
                return;
        }

        new LocalUserDao().edit(user);
    }
}