package usecase.habit;

import core.entity.Habit;
import core.enumiration.Frequency;
import core.exceptions.InvalidFrequencyConversionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, который используется для вычисления серии отметок выбранной привычки
 */
public class HabitStreakService {
    /**
     * Основной метод, который определяет серию отметок
     * @param habit привычка для которой нужно определить серию отметок
     * @return текущую серию отметок
     * @throws InvalidFrequencyConversionException  возникает в случае неудачной конвертации {@link Frequency#convertToInteger(Frequency)
     * convertToInteger()}
     */
    public int getCurrentStreak(Habit habit) throws InvalidFrequencyConversionException {
        List<LocalDateTime> history = habit.getHistory();
        long maxInterval = calculateMaximumIntervalInMinutes();

        if (history.isEmpty()) {
            return 0;
        }

        if (isMissedLastMark(history.get(history.size() - 1), maxInterval)) {
            return 0;
        }

        return calculateStreak(history, maxInterval);
    }

    /**
     * Проверяет, была ли пропущена последняя отметка. Отметка считается пропущенной, если с момента последней отметки прошло
     * более 24 часов (суток).
     * @param lastMarkedDate дата последней отметки
     * @param maxInterval максимально допустимый интервал, при превышении которого сбивается серия отметок
     * @return <b>true</b>: если отметка пропущена, иначе <b>false</b>
     */
    private boolean isMissedLastMark(LocalDateTime lastMarkedDate, long maxInterval) {
        return Duration.between(lastMarkedDate, LocalDateTime.now()).toMinutes() >= maxInterval;
    }

    /**
     * Вычисляет серию отметок, используя {@link Habit#getHistory() history}, итерируется с конца списка до тех пор, пока
     * серия не собьётся
     * @param history список, который содержит историю отметок привычки
     * @param maxInterval максимально допустимый интервал, при превышении которого сбивается серия отметок
     * @return число отметок
     */
    private int calculateStreak(List<LocalDateTime> history, long maxInterval) {
        int streakCounter = 1;

        for (int i = history.size() - 1; i > 0; i--) {
            LocalDateTime dateTime1 = history.get(i);
            LocalDateTime dateTime2 = history.get(i-1);

            if (Duration.between(dateTime1, dateTime2).toMinutes() >= maxInterval) {
                return streakCounter;
            }

            streakCounter++;
        }

        return streakCounter;
    }

    /**
     * Вспомогательный метод для возвращения фиксированного интервала в 24 часа (сутки) в минутах.
     * @return интервал в минутах (24 часа = 1440 минут)
     */
    private long calculateMaximumIntervalInMinutes() {
        return 1440L;
    }
}