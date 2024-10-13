package adapters.in;

import adapters.console.Constants;
import infrastructure.dto.HabitDto;

import java.util.Scanner;

public class HabitCreationInput {
    public HabitDto input(Scanner scanner) throws InterruptedException {
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