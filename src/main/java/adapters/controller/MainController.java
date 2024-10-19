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
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.user.UserDao;
import core.UserAccessService;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class MainController {
    private final UserDao userDao;
    private final HabitDao habitDao;

    /**
     * Метод, отвечающий за выбор контроллера
     * @param scanner экземпляр сканера
     * @param user сущность пользователя, которая взаимодействует с приложением
     * @throws InvalidUserInformationException исключение, выбрасываемое при некорректной информации сущности пользователя
     * @throws InvalidFrequencyConversionException исключение, выбрасываемое при некорректной конвертации {@link core.enumiration.Frequency
     * Frequency}
     */
    public void handle(Scanner scanner, User user) throws InvalidUserInformationException, InvalidFrequencyConversionException {
        while (true) {
            if (!UserAccessService.hasAccess(userDao, user)) {
                return;
            }

            HabitMarkService.checkAllMarks(user);
            chooseStrategyAccordingRole(user);

            String input = scanner.nextLine();
            switch (input) {
                case "1", "1.", "Управление привычками", "1. Управление привычками":
                    new HabitMenuController(habitDao).handle(scanner, user);
                    break;
                case "2", "2.", "Статистика пользователя", "2. Статистика пользователя":
                    break;
                case "3", "3.", "Настройки учётной записи", "3. Настройки учётной записи":
                    new UserMenuController(userDao).handle(scanner, user);
                    break;
                case "4", "4.", "Панель администратора", "4. Панель администратора":
                    if (user.getRole() == Role.ADMINISTRATOR) {
                        new AdministratorMenuController(userDao).handle(scanner);
                    }
                    break;
                case "0", "0.", "Выйти из учётной записи", "0. Выйти из учётной записи":
                    System.out.println("Выход из учётной записи...");
                    user.setAuthorized(false);
                    userDao.edit(user);
                    return;
                default:
                    System.out.println("Указана некорректная опция!");
            }
        }
    }

    /**
     * Выбирает вывод в коносль, в зависимости от роли пользователя
     * @param user сущность пользователя с которым происходит взаимодействие
     */
    private void chooseStrategyAccordingRole(User user) {
        if (user.getRole() == Role.ADMINISTRATOR) {
            System.out.print(Constants.ADMINISTRATOR_MENU);
        } else if (user.getRole() == Role.USER) {
            System.out.print(Constants.MAIN_MENU);
        }
    }
}