package adapters.controller.user;

import adapters.console.Constants;
import adapters.out.UserInformationOutput;
import core.ExpiredHabitMarkService;
import core.exceptions.InvalidUserInformationException;
import core.entity.User;
import infrastructure.dao.user.UserDao;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/**
 * <p>Контроллер, используемый для изменения информации об учётной записи</p>
 * <p>Вызывает следующие сервисы при своей работе: {@link UserDao}</p>
 */
@RequiredArgsConstructor
public class UserMenuController {
    private final UserDao userDao;

    public void handle(Scanner scanner, User user) throws InvalidUserInformationException {
        while (true) {
            ExpiredHabitMarkService.checkAllMarks(user);
            System.out.print(Constants.USER_SETTINGS);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "Изменить имя", "1. Изменить имя":
                    System.out.print("Укажите новое имя: ");
                    user.setUsername(scanner.nextLine());
                    System.out.println("Имя изменено");
                    break;
                case "2", "2.", "Изменить пароль", "2. Изменить пароль":
                    System.out.print("Укажите новый пароль: ");
                    user.setPassword(scanner.nextLine());
                    System.out.println("Пароль изменен");
                    break;
                case "3", "3.", "Изменить электронную почту", "3. Изменить электронную почту":
                    System.out.print("Укажите новую электронную почту: ");
                    user.setEmail(scanner.nextLine());
                    System.out.println("Электронная почта изменена");
                    break;
                case "4", "4.", "Информация об учётной записи", "4. Информация об учётной записи":
                    new UserInformationOutput().output(user);
                    System.out.print("Выберите опцию: ");
                    scanner.nextLine();
                    return;
                case "5", "5.", "Удалить учётную запись", "5. Удалить учётную запись":
                    userDao.delete(user);
                    System.out.println("Удаление учётной записи...");
                    return;
                case "0", "0.", "Вернуться в главное меню", "0. Вернуться в главное меню":
                    return;
                default:
                    System.out.println("Указана некорректная опция!");
                    return;
            }

            userDao.edit(user);
        }
    }
}