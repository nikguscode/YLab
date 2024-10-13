package adapters.controller.habit;

import adapters.console.Constants;
import adapters.in.HabitCreationInput;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import infrastructure.dto.HabitDto;
import usecase.habit.HabitCreator;

import java.util.Scanner;

public class HabitMenuController {
    public void handle(Scanner scanner, User user) throws InterruptedException, InvalidFrequencyConversionException {
        while (true) {
            System.out.print(Constants.HABIT_MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "Список привычек", "1. Список привычек":
                    new HabitListController().handle(scanner, user);
                    break;
                case "2", "2.", "Добавить привычку", "2. Добавить привычку":
                    try {
                        HabitDto habitDto = new HabitCreationInput().input(scanner);
                        addHabitInDatabase(new HabitCreator().create(user, habitDto), user);
                    } catch (InvalidFrequencyConversionException e) {
                        System.out.println("Некорректное значение частоты привычки!");
                    } catch (InvalidHabitInformationException ignored) {
                    }
                    break;
                case "0", "0.", "Вернуться в главное меню", "0. Вернуться в главное меню":
                    return;
                default:
                    System.out.println("Указана некорректная опция!");
            }
        }
    }

    private void addHabitInDatabase(Habit habit, User user) throws InterruptedException {
        user.getHabits().put(habit.getId(), habit);
        System.out.println("Привычка добавлена...");
        Thread.sleep(500);
    }
}
