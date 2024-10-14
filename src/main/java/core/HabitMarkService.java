package core;

import core.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Один из основных сервисов, необходим для обновления состояния привычки. Также, в случае, если привычка не выполнена,
 * выводит уведомление
 */
public class HabitMarkService {
    /**
     * Счётчик невыполненных привычек
     */
    public static int unmarkedHabits = 0;

    /**
     * Основной метод класса, необходим для проверки состояния привычки. Если интервал между датой, когда должна быть
     * сделана отметка и текущей датой превысит сутки, статус привычки будет изменён на не выполнена
     *
     * @param user пользователь у которого необходимо проверить статус привычек
     */
    public static void checkAllMarks(User user) {
        user.getHabits().values().forEach(e -> {
            Duration difference = Duration.between(e.getShiftedDateAndTime(), LocalDateTime.now());
            if (difference.toMinutes() >= 1440L) {
                e.setCompleted(false);
                unmarkedHabits++;
                notifyUser();
            }
        });
    }

    /**
     * Метод для вывода информации о неотмеченных привычках
     */
    private static void notifyUser() {
        if (unmarkedHabits > 0) {
            System.out.printf("[!!!] Количество неотмеченных привычек: %s\n", unmarkedHabits);
        }
    }
}