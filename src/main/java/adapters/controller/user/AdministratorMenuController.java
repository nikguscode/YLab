package adapters.controller.user;

import adapters.controller.habit.HabitListController;
import core.LocalDateTimeFormatter;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.UserDatabase;
import core.entity.User;
import core.enumiration.Role;
import infrastructure.dao.user.UserDao;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/**
 * <p>Контроллер, используемый администратором, для управления пользователями и их привычками</p>
 * <p>Вызывает следующие сервисы при своей работе: {@link UserDao}</p>
 */
@RequiredArgsConstructor
public class AdministratorMenuController {
    private final UserDao userDao;

    public void handle(Scanner scanner) throws InvalidUserInformationException, InvalidFrequencyConversionException {
        while (true) {
            User user = chooseUserAccount(scanner);
            if (user == null) {
                return;
            }
            String input = printMenuForInteractingWithUserAccount(scanner, user);

            switch (input) {
                case "1", "1.", "Управление привычками пользователя", "1. Управление привычками пользователя":
                    new HabitListController().handle(scanner, user);
                    break;
                case "2", "2.", "Редактировать информацию пользователя", "2. Редактировать информацию пользователя":
                    new UserMenuController(userDao).handle(scanner, user);
                    break;
                case "3", "3.", "Заблокировать пользователя", "3. Заблокировать пользователя", "Разблокировать пользователя", "3. Разблокировать пользователя":
                    if (user.getRole() != Role.BLOCKED) {
                        user.setRole(Role.BLOCKED);
                    } else {
                        user.setRole(Role.ADMINISTRATOR);
                    }
                    userDao.edit(user);
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

        switch (input) {
            case "0", "0. ", "Вернуться назад", "0. Вернуться назад" -> {
                return null;
            }
            default -> {
                return userDao.get(input);
            }
        }
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
        System.out.println(user.getRole() != Role.BLOCKED ? "3. Заблокировать пользователя" : "3. Разблокировать пользователя");
        System.out.println("4. Удалить аккаунт пользователя");
        System.out.println("0. Вернуться назад");
        System.out.print("Выберите опцию: ");
        return scanner.nextLine();
    }

    /**
     * Метод, который выводит все учётные записи
     */
    private void printListOfUsers() {
        userDao.get().values().forEach(e -> System.out.printf(
                "Почта: %s | Количество привычек: %s | Дата регистрации: %s | Дата последней авторизации: %s\n",
                e.getEmail(),
                e.getHabits().values().size(),
                LocalDateTimeFormatter.format(e.getRegistrationDate()),
                LocalDateTimeFormatter.format(e.getAuthorizationDate()))
        );
    }
}