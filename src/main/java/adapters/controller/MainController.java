package adapters.controller;

import adapters.console.Constants;
import adapters.controller.habit.HabitMenuController;
import adapters.controller.user.AdministratorMenuController;
import adapters.controller.user.UserMenuController;
import core.HabitMarkService;
import core.entity.User;
import core.enumiration.Role;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.UserDao;
import core.UserAccessService;

import java.util.Scanner;

/**
 * <p>Главный контроллер приложения, вызывается сразу после аутентификации пользователя</p>
 * <p>Вызывает следующие сервисы при своей работе:
 * <ul>
 *     <li>{@link UserAccessService}</li>
 *     <li>{@link HabitMarkService}</li>
 *     <li>{@link UserDao}</li>
 * </ul>
 * </p>
 */
public class MainController {
    private final UserDao userDao;

    public MainController(UserDao userDao) {
        this.userDao = userDao;
    }

    public void handle(Scanner scanner, User user) throws InterruptedException, InvalidUserInformationException, InvalidFrequencyConversionException {
        while (true) {
            if (!UserAccessService.hasAccess(userDao, user)) {
                return;
            }
            HabitMarkService.checkAllMarks(user);
            chooseStrategyAccordingRole(user);

            String input = scanner.nextLine();
            switch (input) {
                case "1", "1.", "Управление привычками", "1. Управление привычками":
                    Thread.sleep(500);
                    new HabitMenuController().handle(scanner, user);
                    break;
                case "2", "2.", "Статистика пользователя", "2. Статистика пользователя":
                    break;
                case "3", "3.", "Настройки учётной записи", "3. Настройки учётной записи":
                    Thread.sleep(500);
                    new UserMenuController(userDao).handle(scanner, user);
                    break;
                case "4", "4.", "Панель администратора", "4. Панель администратора":
                    if (user.getRole() == Role.ADMINISTRATOR) {
                        new AdministratorMenuController(userDao).handle(scanner);
                    }
                    return;
                case "0", "0.", "Выйти из учётной записи", "0. Выйти из учётной записи":
                    System.out.println("Выход из учётной записи...");
                    user.setAuthorized(false);
                    Thread.sleep(1500);
                    return;
                default:
                    System.out.println("Указана некорректная опция!");
            }
        }
    }

    private void chooseStrategyAccordingRole(User user) {
        if (user.getRole() == Role.ADMINISTRATOR) {
            System.out.print(Constants.ADMINISTRATOR_MENU);
        } else if (user.getRole() == Role.USER) {
            System.out.print(Constants.MAIN_MENU);
        }
    }
}