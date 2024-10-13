package adapters.controller.habit;

import adapters.console.Constants;
import core.entity.Habit;

import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Контроллер, отвечающий за возврат {@link Comparator} для сортировки. Не вызывает сервисы при своей работе, из-за простой логики
 */
public class HabitSortController {
    public Comparator<? super Habit> handle(Scanner scanner) {
        while (true) {
            System.out.print(Constants.HABIT_SORT);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "По статусу (выполнена или нет)", "1. По статусу (выполнена или нет)":
                    return Comparator.comparing(Habit::isCompleted);
                case "2", "2.", "По дате и времени начала отсчёта", "2. По дате и времени начала отсчёта":
                    return Comparator.comparing(Habit::getCreationDateAndTime);
                case "3", "3.", "По дате и времени последней отметки", "3. По дате и времени последней отметки":
                    return Comparator.comparing(Habit::getLastMarkDateAndTime);
                case "4", "4.", "По частоте отметки", "4. По частоте отметки":
                    return Comparator.comparing(Habit::getFrequency);
                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                    return null;
                default:
                    System.out.println("Указана некорректная опция!");
            }
        }
    }
}