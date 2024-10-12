package adapters.controller.user;

import adapters.controller.habit.HabitListController;
import core.LocalDateTimeFormatter;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.UserDatabase;
import infrastructure.dao.user.LocalUserDao;
import core.entity.User;
import core.enumiration.Role;

import java.util.Scanner;

public class AdministratorMenuController {
    public void handle(Scanner scanner) throws InterruptedException, InvalidUserInformationException, InvalidFrequencyConversionException {
        while (true) {
            printListOfUsers();

            System.out.print("Укажите почту пользователя, чтобы внести изменения: ");
            String input = scanner.nextLine();
            User user = new LocalUserDao().get(input);

            System.out.println("1. Управление привычками пользователя");
            System.out.println("2. Редактировать информацию пользователя");
            System.out.println(user.getRole() == Role.USER ? "3. Заблокировать пользователя" : "3. Разблокировать пользователя");
            System.out.println("4. Удалить аккаунт пользователя");
            System.out.println("0. Вернуться назад");
            System.out.print("Выберите опцию: ");
            String adminInput = scanner.nextLine();

            switch (adminInput) {
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
                    new LocalUserDao().delete(user);
                    break;
                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                    return;
                default:
                    System.out.println("Выбрана некорректная опция");
            }
        }
    }

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