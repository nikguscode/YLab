package adapters.in;

import adapters.console.Constants;
import infrastructure.dto.HabitDto;
import lombok.SneakyThrows;

import java.util.Scanner;

/**
 * Данный класс предназначен для заполнения {@link HabitDto} пользовательским вводом и последующей передачей в
 * {@link usecase.habit.HabitCreator HabitCreator}
 */
public class HabitCreationInput implements ConsoleInput<HabitDto> {
    /**
     * Метод, используемый для обработки пользовательского ввода при создании новой привычки
     * @param scanner экземпляр сканера
     * @return DTO класс, содержащий пользовательский ввод
     */
    @SneakyThrows
    @Override
    public HabitDto input(Scanner scanner) {
        HabitDto.Builder habitDto = HabitDto.builder();

        System.out.print("Укажите название привычки: ");
        habitDto.title(scanner.nextLine());

        System.out.print("Укажите описание привычки: ");
        habitDto.description(scanner.nextLine());

        System.out.println("""
        \n-----------------------------------------------------------------------------------------
        Укажите дату и время, относительно которых будет взят старт, в формате: hh:mm dd/mm/yyyy.
        В случае, если ничего не указано, старт будет взят относительно текущей даты и времени!
        Обратите внимание, что дата старта привычки не может быть раньше, чем текущая дата!!!
        -----------------------------------------------------------------------------------------""");
        System.out.print("Дата и время: ");
        habitDto.dateAndTime(scanner.nextLine());

        System.out.print(Constants.FREQUENCY_LIST);
        Thread.sleep(400);
        habitDto.frequency(scanner.nextLine());

        return habitDto.build();
    }
}