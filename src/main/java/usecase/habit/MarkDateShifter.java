
package usecase.habit;

import core.entity.Habit;
import core.exceptions.InvalidFrequencyConversionException;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Данный класс предназначен для сдвига даты и времени отметки, класс предназначен для улучшения читаемости кода
 */
public class MarkDateShifter {
    /**
     * Сдвигает дату следующей отметки привычки относительно даты, когда должна быть совершена отметка.
     * Если отметок ещё не было, рассчитывает следующую отметку от даты создания.
     *
     * @param habit привычка, для которой сдвигается отметка
     * @throws InvalidFrequencyConversionException если произошла ошибка при конвертации частоты
     */
    public void shiftMarkDate(Habit habit) throws InvalidFrequencyConversionException {
        int frequencyInDaysInt = habit.getFrequency().getIntegerValue();

        if (habit.getNextMarkDateAndTime() == null) {
            habit.setNextMarkDateAndTime(LocalDateTime.of(
                    LocalDate.now().plusDays(frequencyInDaysInt),
                    habit.getCreationDateAndTime().toLocalTime()
            ));
        } else {
            LocalDate nextMarkDate = habit.getNextMarkDateAndTime().toLocalDate();

            while (LocalDate.now().isAfter(nextMarkDate)) {
                nextMarkDate = nextMarkDate.plusDays(frequencyInDaysInt);
            }

            habit.setNextMarkDateAndTime(LocalDateTime.of(nextMarkDate, habit.getCreationDateAndTime().toLocalTime()));
        }
    }
}