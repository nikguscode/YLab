package adapters.controller.habit;

import core.ExpiredHabitMarkService;
import core.entity.Habit;
import core.exceptions.InvalidFrequencyConversionException;
import core.entity.User;
import adapters.out.HabitListOutput;
import core.exceptions.InvalidHabitIdException;
import infrastructure.dao.HabitMarkHistory.HabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * <p>Контроллер, отвечающий за взаимодействие со списком привычек</p>
 * <p>Вызывает следующий сервис при своей работе: {@link HabitListOutput}</p>
 */
@RequiredArgsConstructor
public class HabitListController {
    private final HabitDao habitDao;
    private final HabitMarkHistoryDao habitMarkHistoryDao;

    public void handle(Scanner scanner, User user) throws InvalidFrequencyConversionException {
        Predicate<? super Habit> predicate = null;
        Comparator<? super Habit> comparator = null;

        while (true) {
            ExpiredHabitMarkService.checkAllMarks(user);
            user.setHabits(habitDao.getAll(user));
            new HabitListOutput(habitMarkHistoryDao).outputList(user, predicate, comparator);
            String input = scanner.nextLine();

            switch (input) {
                case "#0", "#0.", "Вернуться назад", "#0. Вернуться назад":
                    return;
                case "#1", "#1.", "Сортировка", "#1. Сортировка":
                    comparator = new HabitSortController().handle(scanner);
                    break;
                case "#2", "#2.", "Фильтрация", "#2. Фильтрация":
                    predicate = new HabitFilterController().handle(scanner);
                    break;
                default:
                    try {
                        new HabitSettingsController(habitDao, habitMarkHistoryDao).handle(scanner, user, input);
                    } catch (InvalidHabitIdException e) {
                        System.out.println("Некорректный идентификатор привычки!");
                    }
            }
        }
    }
}