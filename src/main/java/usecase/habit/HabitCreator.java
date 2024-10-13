package usecase.habit;

import adapters.in.HabitCreationInput;
import core.entity.Habit;
import core.enumiration.Frequency;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import infrastructure.dao.user.LocalUserDao;
import infrastructure.dto.HabitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, используемый при создании новой привычки
 */
public class HabitCreator {
    /**
     * Основной метод класса, используется для создания привычки. Конструирует привычку, используя {@link Habit.Builder},
     * а также {@link HabitDto} для передачи пользовательского ввода
     *
     * @param scanner передаёт текущий поток сканера
     * @param email   передаёт почту пользователя, для того, чтобы получить экземпляр User и добавить для него привычку
     * @return созданную пользователем привычку
     * @throws InterruptedException                стандартное исключение, вызываемое из-за задержки вывода
     * @throws InvalidFrequencyConversionException возникает в случае неудачной конвертации {@link Frequency#convertFromString(String)
     *                                             convertFromString()}
     * @throws InvalidHabitInformationException    возникает в том случае, если пользователь ввёл некорректные данные при
     *                                             создании новой привычки или разработчик указал некорректные параметры
     *                                             при создании {@link Habit}
     */
    public Habit create(Scanner scanner, String email) throws InterruptedException, InvalidFrequencyConversionException, InvalidHabitInformationException {
        Habit.Builder habit = Habit.builder();
        HabitDto habitDto = new HabitCreationInput().input(scanner);

        setHabitCreationDateAndTime(habitDto.getDateAndTime(), habit);
        return habit
                .userId(new LocalUserDao().get(email).getId())
                .title(habitDto.getTitle())
                .description(habitDto.getDescription())
                .frequency(Frequency.convertFromString(habitDto.getFrequency()))
                .build();
    }

    /**
     * Метод, отвечающий за установку даты и времени привычки
     * @param input дата, которую вводить пользователь
     * @param habitBuilder билдер привычки для создания {@link Habit}
     * @throws InvalidHabitInformationException возникает в том случае, если пользователь ввёл некорректные данные при
     * создании новой привычки или разработчик указал некорректные параметры при создании {@link Habit}
     */
    private void setHabitCreationDateAndTime(String input, Habit.Builder habitBuilder) throws InvalidHabitInformationException {
        if (input.isEmpty()) {
            setDefaultDateAndTime(habitBuilder);
            return;
        }

        String dateTimePattern = "\\d{2}:\\d{2} \\d{2}/\\d{2}/\\d{4}";
        if (!input.matches(dateTimePattern)) {
            System.out.println("Некорректный формат даты и времени!");
            throw new InvalidHabitInformationException();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime parsedDateTime = LocalDateTime.parse(input, formatter);

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
    private void setDefaultDateAndTime(Habit.Builder habitBuilder) throws InvalidHabitInformationException {
        LocalDateTime now = LocalDateTime.now();
        habitBuilder.creationDateAndTime(now)
                .lastMarkDateAndTime(now)
                .shiftedDateAndTime(now)
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
    private void setCustomDateAndTime(Habit.Builder habitBuilder, LocalDateTime startDateTime) throws InvalidHabitInformationException {
        habitBuilder.creationDateAndTime(LocalDateTime.now())
                .shiftedDateAndTime(startDateTime)
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