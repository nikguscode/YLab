package adapters.controller.user;

import adapters.console.Constants;
import adapters.out.UserInformationOutput;
import core.HabitMarkService;
import core.exceptions.InvalidUserInformationException;
import core.entity.User;
import infrastructure.dao.user.UserDao;

import java.util.Scanner;

/**
 * <p>Контроллер, используемый для изменения информации об учётной записи</p>
 * <p>Вызывает следующие сервисы при своей работе: {@link UserDao}</p>
 */
public class UserMenuController {
    private final UserDao userDao;

    public UserMenuController(UserDao userDao) {
        this.userDao = userDao;
    }

    public void handle(Scanner scanner, User user) throws InterruptedException, InvalidUserInformationException {
        while (true) {
            HabitMarkService.checkAllMarks(user);
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
                    userDao.delete(user);
                    System.out.println("Удаление учётной записи...");
                    Thread.sleep(1000);
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