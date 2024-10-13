package usecase;

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

        setDateAndTime(habitDto.getDateAndTime(), habit);
        habit.frequency(Frequency.convertFromString(habitDto.getFrequency()));

        return habit
                .userId(new LocalUserDao().get(email).getId())
                .history(new ArrayList<>())
                .build();
    }

    /**
     * Метод, определяющий дату и время начала отсчёта привычки, обрабатывает три случая:
     * <ul>
     *     <li>Пользователь не указал дату и время, отсчёт берётся относительно текущей даты и времени</li>
     *     <li>Пользователь указал дату и время и они соответствуют формату: HH:mm dd/MM/yyyy</li>
     *     <li>Пользователь указал дату и время, но они не соответствуют формату: HH:mm dd/MM/yyyy</li>
     * </ul>
     * @param input дата и время, которые указывает пользователь
     * @param habit билдер текущей привычки, чтобы установить дату и время создания
     * @throws InvalidHabitInformationException возникает в том случае, если пользователь ввёл некорректные данные при
     * создании новой привычки или разработчик указал некорректные параметры при создании {@link Habit}
     */
    private void setDateAndTime(String input, Habit.Builder habit) throws InvalidHabitInformationException {
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