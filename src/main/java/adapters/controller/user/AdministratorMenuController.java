package adapters.controller.user;

import adapters.controller.habit.HabitListController;
import core.LocalDateTimeFormatter;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.UserDatabase;
import core.entity.User;
import core.enumiration.Role;
import infrastructure.dao.user.UserDao;

import java.util.Scanner;

/**
 * <p>Контроллер, используемый администратором, для управления пользователями и их привычками</p>
 * <p>Вызывает следующие сервисы при своей работе: {@link UserDao}</p>
 */
public class AdministratorMenuController {
    private final UserDao userDao;

    public AdministratorMenuController(UserDao userDao) {
        this.userDao = userDao;
    }

    public void handle(Scanner scanner) throws InterruptedException, InvalidUserInformationException, InvalidFrequencyConversionException {
        while (true) {
            User user = chooseUserAccount(scanner);
            String input = printMenuForInteractingWithUserAccount(scanner, user);

            switch (input) {
                case "1", "1.", "Управление привычками пользователя", "1. Управление привычками пользователя":
                    new HabitListController().handle(scanner, user.getEmail());
                    break;
                case "2", "2.", "Редактировать информацию пользователя", "2. Редактировать информацию пользователя":
                    new UserMenuController().handle(scanner, user);
                    break;
                case "3", "3.", "Заблокировать пользователя", "3. Заблокировать пользователя", "Разблокировать пользователя", "3. Разблокировать пользователя":
                    user.setRole(Role.BLOCKED);
                    break;
                case "4", "4.", "Удалить аккаунт", "4. Удалить аккаунт":
                    userDao.delete(user);
                    break;
                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                    return;
                default:
                    System.out.println("Выбрана некорректная опция");
            }
        }
    }

    /**
     * Метод, выполняющий обработку пользовательского ввода при выборе учётной записи для редактирования
     * @param scanner экземпляр сканера
     * @return сущность пользователя, которую нужно редактировать
     */
    private User chooseUserAccount(Scanner scanner) {
        printListOfUsers();
        System.out.println("0. Вернуться назад");
        System.out.print("Укажите почту пользователя, чтобы внести изменения: ");
        String input = scanner.nextLine();
        return userDao.get(input);
    }

    /**
     * Метод, отображающий меню для взаимодействия с учётной записью выбранного пользователя
     * @param scanner экземпляр сканера
     * @param user сущность пользователя с которой происходит взаимодействие
     * @return пользовательский ввод администратора при взаимодействии с меню
     */
    private String printMenuForInteractingWithUserAccount(Scanner scanner, User user) {
        System.out.println("1. Управление привычками пользователя");
        System.out.println("2. Редактировать информацию пользователя");
        System.out.println(user.getRole() == Role.USER ? "3. Заблокировать пользователя" : "3. Разблокировать пользователя");
        System.out.println("4. Удалить аккаунт пользователя");
        System.out.println("0. Вернуться назад");
        System.out.print("Выберите опцию: ");
        return scanner.nextLine();
    }

    /**
     * Метод, который выводит все учётные записи
     */
    private void printListOfUsers() {
        UserDatabase.database.values().forEach(e -> System.out.printf(
                "Почта: %s | Количество привычек: %s | Дата регистрации: %s | Дата последней авторизации: %s\n",
                e.getEmail(),
                e.getHabits().values().size(),
                LocalDateTimeFormatter.format(e.getRegistrationDate()),
                LocalDateTimeFormatter.format(e.getAuthorizationDate()))
        );
    }
}