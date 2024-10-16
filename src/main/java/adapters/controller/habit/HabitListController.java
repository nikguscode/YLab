package adapters.controller.habit;

import core.HabitMarkService;
import core.entity.Habit;
import core.exceptions.InvalidFrequencyConversionException;
import core.entity.User;
import adapters.out.HabitListOutput;
import core.exceptions.InvalidHabitIdException;

import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * <p>Контроллер, отвечающий за взаимодействие со списком привычек</p>
 * <p>Вызывает следующий сервис при своей работе: {@link HabitListOutput}</p>
 */
public class HabitListController {
    public void handle(Scanner scanner, User user) throws InterruptedException, InvalidFrequencyConversionException {
        Predicate<? super Habit> predicate = null;
        Comparator<? super Habit> comparator = null;

        while (true) {
            HabitMarkService.checkAllMarks(user);
            new HabitListOutput().outputList(user, predicate, comparator);
            String input = scanner.nextLine();

            switch (input) {
                case "#0", "#0.", "Вернуться назад", "#0. Вернуться назад":
                    Thread.sleep(500);
                    return;
                case "#1", "#1.", "Сортировка", "#1. Сортировка":
                    comparator = new HabitSortController().handle(scanner);
                    break;
                case "#2", "#2.", "Фильтрация", "#2. Фильтрация":
                    predicate = new HabitFilterController().handle(scanner);
                    break;
                default:
                    try {
                        new HabitSettingsController().handle(scanner, user, input);
                    } catch (InvalidHabitIdException e) {
                        System.out.println("Некорректный идентификатор привычки!");
                        throw new RuntimeException(e);
                    }
            }
        }
    }
}