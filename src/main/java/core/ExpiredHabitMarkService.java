package core;

import core.entity.Habit;
import core.entity.User;
import infrastructure.DatabaseUtils;
import infrastructure.dao.habitmarkhistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.habit.JdbcHabitDao;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Один из основных сервисов, необходим для обновления состояния привычки. Также, в случае, если привычка не выполнена,
 * выводит уведомление
 */
public class ExpiredHabitMarkService {
    /**
     * Проверяет состояние привычки. Если интервал между датой, когда должна быть сделана отметка и текущей датой превысит
     * сутки, статус привычки будет изменён на не выполнена
     *
     * @param user пользователь у которого необходимо проверить статус привычек
     */
    public static void checkAllMarks(User user) {
        int unmarkedHabits = 0;

        for (Habit habit : user.getHabits().values()) {
            Duration difference = Duration.between(habit.getNextMarkDateAndTime(), LocalDateTime.now());
            if (difference.toMinutes() >= 1440L) {
                habit.setCompleted(false);
                editUser(habit);
                unmarkedHabits++;
            }
        }
        notifyUser(unmarkedHabits);
    }

    /**
     * Выводит информации о неотмеченных привычках
     */
    private static void notifyUser(int unmarkedHabits) {
        if (unmarkedHabits > 0) {
            System.out.printf("[!!!] Количество неотмеченных привычек: %s\n", unmarkedHabits);
        }
    }

    /**
     * Изменяет состояние привычки на невыполненную
     * @param habit привычка для которой необходимо изменить состояние
     */
    private static void editUser(Habit habit) {
        ConfigLoaderService configLoader = ConfigLoaderService.getInstance();
        DatabaseUtils databaseUtils = new DatabaseUtils(
                configLoader.getProperties("datasource.driver"),
                configLoader.getProperties("datasource.url"),
                configLoader.getProperties("datasource.username"),
                configLoader.getProperties("datasource.password")
        );

        new JdbcHabitDao(databaseUtils, new JdbcHabitMarkHistoryDao(databaseUtils)).edit(habit);
    }
}