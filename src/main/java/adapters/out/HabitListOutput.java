package adapters.out;

import adapters.console.Constants;
import infrastructure.dao.HabitMarkHistory.HabitMarkHistoryDao;
import lombok.RequiredArgsConstructor;
import usecase.habit.HabitStreakService;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Данный класс предназначен для вывода всех привычек, в зависимости от наличия/отсутствия {@link Predicate} и {@link Comparator}
 * выбирается стратегия для вывода привычек
 */
@RequiredArgsConstructor
public class HabitListOutput {
    private final HabitMarkHistoryDao habitMarkHistoryDao;

    /**
     * Основной метод для вывода привычек, осуществляет выбор стратегии для вывода
     * @param user пользователь для которого необходимо вывести привычки
     * @param predicate условие фильтрации
     * @param comparator компаратор для сортировки
     */
    public void outputList(User user, Predicate<? super Habit> predicate, Comparator<? super  Habit> comparator) {
        System.out.println(Constants.HABIT_LIST);
        if (predicate == null && comparator == null) {
            outputWithoutChanges(user);
        } else if (predicate != null){
            filter(user, predicate);
        } else {
            sort(user, comparator);
        }
        System.out.print(Constants.FILTER_FOOTER);
    }

    /**
     * Стаднартный вывод списка привычек без фильтрации и сортировки
     * @param user пользователь для которого необходимо вывести привычки
     */
    private void outputWithoutChanges(User user) {
        user.getHabits().values()
                .forEach(e -> {
                    checkHabitStreak(e);
                    printHabitInformation(e);
                });
    }

    /**
     * Выводит отфильтрованный список привычек
     * @param user пользователь для которого необходимо вывести привычки
     * @param predicate условие, определяющие, какие привычки будут выведены
     */
    private void filter(User user, Predicate<? super Habit> predicate) {
        user.getHabits().values().stream()
                .filter(predicate)
                .forEach(e -> {
                    checkHabitStreak(e);
                    printHabitInformation(e);
                });
    }

    /**
     * Выводит отсортированный список привычек
     * @param user пользователь для которого необходимо вывести привычки
     * @param comparator компаратор, определяющий, каким образом будут отсортированы привычки
     */
    private void sort(User user, Comparator<? super Habit> comparator) {
        user.getHabits().values().stream()
                .sorted(comparator)
                .forEach(e -> {
                    checkHabitStreak(e);
                    printHabitInformation(e);
                });
    }

    /**
     * Метод, определяющий серию выполнения привычки (streak), используя {@link HabitStreakService}. Метод необходим
     * только для улучшения читаемости кода
     * @param habit привычка для которой нужно оперелить серию
     */
    private void checkHabitStreak(Habit habit) {
        try {
            habit.setStreak(new HabitStreakService(habitMarkHistoryDao).getCurrentStreak(habit));
        } catch (InvalidFrequencyConversionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Выводит отформатированную привычку. Метод необходим только для улучшения читаемости кода
     * @param habit привычка которую нужно вывести
     */
    private void printHabitInformation(Habit habit) {
        String currentHabit = String.format(
                "ID: %s | Название: %s | Статус: %s | Частота: %s | Streak: %s",
                habit.getId(),
                habit.getTitle(),
                habit.isCompleted() ? "Выполнена" : "Не выполнена",
                habit.getFrequency().getStringValue(),
                habit.getStreak()
        );
        System.out.println(currentHabit);
    }
}