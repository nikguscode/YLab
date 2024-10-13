package adapters.controller.habit;

import core.entity.Habit;
import core.exceptions.InvalidFrequencyConversionException;
import core.entity.User;
import adapters.out.HabitListOutput;

import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Predicate;

public class HabitListController {
    public void handle(Scanner scanner, User user) throws InterruptedException, InvalidFrequencyConversionException {
        Predicate<? super Habit> predicate = null;
        Comparator<? super Habit> comparator = null;

        while (true) {
            new HabitListOutput().outList(user, predicate, comparator);
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
                    new HabitSettingsController().handle(scanner, user, input);
            }
        }
    }
}