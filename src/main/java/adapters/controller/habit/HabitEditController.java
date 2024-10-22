package adapters.controller.habit;

import adapters.console.Constants;
import core.ExpiredHabitMarkService;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import core.entity.Habit;
import core.entity.User;
import core.enumiration.Frequency;
import infrastructure.dao.habit.HabitDao;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/**
 * <p>Контроллер, отвечающий за редактирование привычки</p>
 * <p>Вызывает следующий сервис при своей работе: {@link ExpiredHabitMarkService}
 */
@RequiredArgsConstructor
public class HabitEditController {
    private final HabitDao habitDao;

    public void handle(Scanner scanner, User user, Habit currentHabit) throws InvalidFrequencyConversionException, InvalidHabitInformationException {
        while (true) {
            ExpiredHabitMarkService.checkAllMarks(user);
            System.out.print(Constants.HABIT_EDIT_MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "Изменить название", "1. Изменить название":
                    System.out.print("Введите новое название: ");
                    currentHabit.setTitle(scanner.nextLine());
                    habitDao.edit(currentHabit);
                    return;
                case "2", "2.", "Изменить описание", "2. Изменить описание":
                    System.out.println("Введите новое описание: ");
                    currentHabit.setDescription(scanner.nextLine());
                    habitDao.edit(currentHabit);
                    return;
                case "3", "3.", "Изменить частоту", "3. Изменить частоту":
                    System.out.println(Constants.FREQUENCY_LIST);
                    currentHabit.setFrequency(Frequency.convertFromString(scanner.nextLine()));
                    habitDao.edit(currentHabit);
                    return;
                case "4", "4.", "Удалить привычку", "4. Удалить привычку":
                    habitDao.delete(currentHabit);
                    return;
                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                    return;
                default:
                    System.out.println("Выбрана некорректная опция!");
            }
        }
    }
}