package core;

import core.entity.Habit;
import core.entity.User;
import infrastructure.DatabaseUtils;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habitmarkhistory.impl.JdbcHabitMarkHistoryDao;
import infrastructure.dao.habit.impl.JdbcHabitDao;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Один из основных сервисов, необходим для обновления состояния привычки. Также, в случае, если привычка не выполнена,
 * выводит уведомление
 */
public class ExpiredHabitMarkService {
    private final static HabitDao habitDao = new JdbcHabitDao(
            new DatabaseUtils(),
            new JdbcHabitMarkHistoryDao(new DatabaseUtils())
    );

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
                habitDao.edit(habit);
                unmarkedHabits++;
            }
        }
        notifyUser(unmarkedHabits);
    }

    /**
     * Выводит информации о неотмеченных привычках
     * При использовании Spring Boot будет изменана реализация для отправки уведомления пользователю API
     */
    @Deprecated
    private static void notifyUser(int unmarkedHabits) {
        if (unmarkedHabits > 0) {
            System.out.printf("[!!!] Количество неотмеченных привычек: %s\n", unmarkedHabits);
        }
    }
}