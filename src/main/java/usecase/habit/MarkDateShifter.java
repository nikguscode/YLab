package usecase.habit;

import core.entity.Habit;
import core.enumiration.Frequency;
import core.exceptions.InvalidFrequencyConversionException;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Данный класс предназначен для сдвига даты и времени отметки, класс предназначен для улучшения читаемости кода
 */
public class MarkDateShifter {
    public void shiftMarkDate(Habit habit) throws InvalidFrequencyConversionException {
        int frequencyInDaysInt = Frequency.convertToInteger(habit.getFrequency());

        habit.setShiftedDateAndTime(LocalDateTime.of(
                LocalDate.now().plusDays(frequencyInDaysInt),
                habit.getCreationDateAndTime().toLocalTime()
        ));
    }
}