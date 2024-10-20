package usecase.habit;

import core.entity.Habit;
import core.enumiration.Frequency;
import core.exceptions.InvalidFrequencyConversionException;
import infrastructure.dao.HabitMarkHistory.HabitMarkHistoryDao;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, который используется для вычисления серии отметок выбранной привычки
 */
@RequiredArgsConstructor
public class HabitStreakService {
    private final HabitMarkHistoryDao habitMarkHistoryDao;

    /**
     * Основной метод, который определяет серию отметок
     * @param habit привычка для которой нужно определить серию отметок
     * @return текущую серию отметок
     * @throws InvalidFrequencyConversionException  возникает в случае неудачной конвертации
     */
    public int getCurrentStreak(Habit habit) throws InvalidFrequencyConversionException {
        List<LocalDateTime> history = habitMarkHistoryDao.getAll(habit);
        long maxInterval = calculateMaximumIntervalInMinutes(habit.getFrequency());

        if (history.isEmpty()) {
            return 0;
        }

        if (isMissedLastMark(history.get(history.size() - 1), maxInterval)) {
            return 0;
        }

        return calculateStreak(history, maxInterval);
    }

    /**
     * Проверяет, была ли пропущена последняя отметка. Отметка считается пропущенной, если превышает частоту более чем
     * на 1 день. Пример: последняя отметка была 08:00 10/10/2024, частота: еженедельно => следующая отметка должна быть
     * в 08:00 17/10/2024, при этом у пользователя есть время до 8:00 18/10/24, пока не настанет время ещё одной отметки
     * @param lastMarkedDate дата последней отметки
     * @param maxInterval максимально допустимый интервал, при привышении которого сбивается серия отметок
     * @return <b>true</b>: если отметка пропущена, иначе <b>false</b>
     */
    private boolean isMissedLastMark(LocalDateTime lastMarkedDate, long maxInterval) {
        return Duration.between(lastMarkedDate, LocalDateTime.now()).toMinutes() >= maxInterval;
    }

    /**
     * Вычисляет серию отметок, используя {@link Habit#getHistory() history}, итерируется с конца списка до тех пор, пока
     * серия не собьётся
     * @param history список, который содержит историю отметок привычки
     * @param maxInterval максимально допустимый интервал, при привышении которого сбивается серия отметок
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
     * Вспомогательный метод, для перевода дней в минуты. Используется для повышения читаемости кода
     * @param frequency частота привычки
     * @return переведенный интервал в минутах
     */
    private long calculateMaximumIntervalInMinutes(Frequency frequency) {
        return 1440L * (frequency.getIntegerValue() + 1);
    }
}