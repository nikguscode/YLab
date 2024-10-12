package usecase;

import adapters.in.HabitCreationInput;
import core.enumiration.Frequency;
import core.exceptions.InvalidFrequencyConversionException;
import infrastructure.dao.user.LocalUserDao;
import core.entity.Habit;
import infrastructure.dto.HabitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Класс, используемый при создании новой привычки
 */
public class HabitCreator {
    public Habit create(Scanner scanner, String email) throws InterruptedException, InvalidFrequencyConversionException {
        Habit.Builder habit = Habit.builder();
        HabitDto habitDto = new HabitCreationInput().input(scanner);

        setDateAndTime(habitDto.getDateAndTime(), habit);
        habit.frequency(Frequency.convertFromString(habitDto.getFrequency()));

        return habit
                .userId(new LocalUserDao().get(email).getId())
                .history(new ArrayList<>())
                .build();
    }

    private void setDateAndTime(String input, Habit.Builder habit) {
        String dateTimePattern = "\\d{2}:\\d{2} \\d{2}/\\d{2}/\\d{4}";

        if (input.isEmpty()) {
            habit.creationDateAndTime(LocalDateTime.now())
                    .lastMarkDateAndTime(LocalDateTime.now())
                    .shiftedDateAndTime(LocalDateTime.now());
        } else if (input.matches(dateTimePattern)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            habit.creationDateAndTime(LocalDateTime.parse(input, formatter))
                    .lastMarkDateAndTime(LocalDateTime.parse(input, formatter))
                    .shiftedDateAndTime(LocalDateTime.parse(input, formatter));
        } else {
            System.out.println("Некорректный формат даты и времени!");
        }
    }
}