package adapters.controller.habit;

import adapters.console.Constants;
import core.HabitMarkService;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import core.entity.Habit;
import core.entity.User;
import core.enumiration.Frequency;

import java.util.Scanner;

/**
 * <p>Контроллер, отвечающий за редактирование привычки</p>
 * <p>Вызывает следующий сервис при своей работе: {@link HabitMarkService}
 */
public class HabitEditController {
    public void handle(Scanner scanner, User user, Habit currentHabit) throws InvalidFrequencyConversionException, InvalidHabitInformationException {
        while (true) {
            HabitMarkService.checkAllMarks(user);
            System.out.println(Constants.HABIT_EDIT_MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "Изменить название", "1. Изменить название":
                    System.out.print("Введите новое название: ");
                    currentHabit.setTitle(scanner.nextLine());
                    break;
                case "2", "2.", "Изменить описание", "2. Изменить описание":
                    System.out.println("Введите новое описание: ");
                    currentHabit.setDescription(scanner.nextLine());
                    break;
                case "3", "3.", "Изменить частоту", "3. Изменить частоту":
                    System.out.println(Constants.FREQUENCY_LIST);
                    currentHabit.setFrequency(Frequency.convertFromString(scanner.nextLine()));
                    break;
                case "4", "4.", "Удалить привычку", "4. Удалить привычку":
                    user.getHabits().remove(currentHabit.getId());
                    break;
                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                    return;
                default:
                    System.out.println("Выбрана некорректная опция!");
            }
        }
    }
}