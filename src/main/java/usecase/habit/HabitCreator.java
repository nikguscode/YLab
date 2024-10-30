package usecase.habit;

import core.entity.Habit;
import core.entity.User;
import core.exceptions.usecase.InvalidFrequencyConversionException;
import core.exceptions.usecase.InvalidHabitInformationException;
import common.dto.request.habit.HabitPostDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, используемый при создании новой привычки
 */
public class HabitCreator {
    /**
     * Используется для создания привычки. Конструирует привычку, используя {@link Habit.HabitBuilder},
     * а также {@link HabitPostDto} для получения пользовательского ввода
     *
     * @param user сущность текушего пользователя
     * @param habitPostDto dto, содержащий пользовательский ввод при создании привычки
     * @return созданную пользователем привычку
     * @throws InvalidHabitInformationException    возникает в том случае, если пользователь ввёл некорректные данные при
     *                                             создании новой привычки или разработчик указал некорректные параметры
     *                                             при создании {@link Habit}
     */
    public Habit create(User user, HabitPostDto habitPostDto) throws InvalidFrequencyConversionException, InvalidHabitInformationException {
        Habit.HabitBuilder habit = Habit.builder();

        setHabitCreationDateAndTime(habitPostDto.getMarkDateAndTime(), habit);
        return habit
                .userId(user.getId())
                .title(habitPostDto.getTitle())
                .description(habitPostDto.getDescription())
                .frequency(habitPostDto.getFrequency())
                .build();
    }

    /**
     * Устанавливает дату и время привычки, при помощи {@link HabitCreator#setDefaultDateAndTime(Habit.HabitBuilder)
     * setDefaultDateAndTime()}, {@link HabitCreator#setCustomDateAndTime(Habit.HabitBuilder, LocalDateTime) setCustomDateAndTime},
     * {@link HabitCreator#isStartDateBeforeNow(LocalDateTime) isStartDateBeforeNow()}
     * @param habitBuilder билдер привычки для создания {@link Habit}
     * @throws InvalidHabitInformationException возникает в том случае, если пользователь ввёл некорректные данные при
     * создании новой привычки или разработчик указал некорректные параметры при создании {@link Habit}
     */
    private void setHabitCreationDateAndTime(String dateAndTime, Habit.HabitBuilder habitBuilder) throws InvalidHabitInformationException {
        if (dateAndTime == null || dateAndTime.isEmpty()) {
            setDefaultDateAndTime(habitBuilder);
            return;
        }

        String dateTimePattern = "\\d{2}:\\d{2} \\d{2}/\\d{2}/\\d{4}";
        if (!dateAndTime.matches(dateTimePattern)) {
            System.out.println("Некорректный формат даты и времени!");
            throw new InvalidHabitInformationException();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateAndTime, formatter);

        if (isStartDateBeforeNow(parsedDateTime)) {
            System.out.println("Дата старта привычки не может быть раньше, чем текущая дата!");
            throw new InvalidHabitInformationException();
        }

        setCustomDateAndTime(habitBuilder, parsedDateTime);
    }

    /**
     * Стратегия, которая используется, если пользователь не ввёл дату и время старта привычки
     * @param habitBuilder билдер привычки для создания {@link Habit}
     * @throws InvalidHabitInformationException возникает в том случае, если пользователь ввёл некорректные данные при
     * создании новой привычки или разработчик указал некорректные параметры при создании {@link Habit}
     */
    private void setDefaultDateAndTime(Habit.HabitBuilder habitBuilder) throws InvalidHabitInformationException {
        LocalDateTime now = LocalDateTime.now();
        habitBuilder.creationDateAndTime(now)
                .lastMarkDateAndTime(now)
                .isCompleted(true)
                .history(new ArrayList<>(List.of(now)));
    }

    /**
     * Стратегия, которая используется, если пользователь ввёл дату и время старта привычки
     * @param habitBuilder билдер привычки для создания {@link Habit}
     * @param startDateTime дата и время старта привычки
     * @throws InvalidHabitInformationException возникает в том случае, если пользователь ввёл некорректные данные при
     * создании новой привычки или разработчик указал некорректные параметры при создании {@link Habit}
     */
    private void setCustomDateAndTime(Habit.HabitBuilder habitBuilder, LocalDateTime startDateTime) throws InvalidHabitInformationException {
        habitBuilder.creationDateAndTime(LocalDateTime.now())
                .nextMarkDateAndTime(startDateTime)
                .isCompleted(false)
                .history(new ArrayList<>());
    }

    /**
     * Проверяет, что дата и время старта привычки позже либо равны текущей дате
     * @param startDateTime дата и время старта привычки
     * @return <b>true</b>: если дата и время старта привычки позже либо равны текущей дате, иначе <b>false</b>
     */
    private boolean isStartDateBeforeNow(LocalDateTime startDateTime) {
        return LocalDateTime.now().isAfter(startDateTime);
    }
}