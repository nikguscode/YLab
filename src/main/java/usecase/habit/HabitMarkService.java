package usecase.habit;

import common.enumiration.Frequency;
import core.entity.Habit;
import core.exceptions.usecase.InvalidFrequencyConversionException;
import infrastructure.dao.habitmarkhistory.HabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Отвечает за отмечку привычки и все действия, которые должны выполниться в момент отметки
 */
@RequiredArgsConstructor
public class HabitMarkService {
    private final HabitDao habitDao;
    private final HabitMarkHistoryDao habitMarkHistoryDao;

    /**
     * Выполняет отметку, указанной привычки
     * @param habit привычка для которой выполняется отметка
     * @throws InvalidFrequencyConversionException некорректное преобразование из {@link String} в {@link Frequency
     * Frequency}
     */
    public void mark(Habit habit) throws InvalidFrequencyConversionException {
        if (habit.isCompleted()) {
            System.out.println("Ошибка, привычка уже выполнена, ожидайте её сброса!");
            return;
        }

        if (habit.getNextMarkDateAndTime() != null &&
                habit.getNextMarkDateAndTime().isAfter(LocalDateTime.now())) {
            System.out.println("Ошибка, время отметки ещё не настало!");
            return;
        }

        editHabit(habit);
        editMarkHistory(habit);
        System.out.println("Привычка отмечена как выполненная...");
    }

    /**
     * Обновляет статус, дату последней отметки и дату следующей отметки для привычки
     * @param habit привычка, которую необходимо обновить
     * @throws InvalidFrequencyConversionException некорректное преобразование из {@link String} в {@link Frequency
     * Frequency}
     */
    private void editHabit(Habit habit) throws InvalidFrequencyConversionException {
        habit.setCompleted(true);
        habit.setLastMarkDateAndTime(LocalDateTime.now());
        new MarkDateShifter().shiftMarkDate(habit);
        habitDao.edit(habit);
    }

    /**
     * Обновляет историю отметок для привычки
     * @param habit привычка у которой необходимо обновить историю отметок
     */
    private void editMarkHistory(Habit habit) {
        habitMarkHistoryDao.add(habit, LocalDateTime.now());
        habit.setHistory(habitMarkHistoryDao.getAll(habit));
    }
}