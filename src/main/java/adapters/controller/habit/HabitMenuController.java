package adapters.controller.habit;

import adapters.console.Constants;
import usecase.HabitCreator;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;
import infrastructure.dao.user.LocalUserDao;

import java.util.Scanner;

public class HabitMenuController {
    public void handle(Scanner scanner, String email) throws InterruptedException, InvalidFrequencyConversionException {
        while (true) {
            System.out.print(Constants.HABIT_MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "Список привычек", "1. Список привычек":
                    new HabitListController().handle(scanner, email);
                    break;
                case "2", "2.", "Добавить привычку", "2. Добавить привычку":
                    addHabitInDatabase(new HabitCreator().create(scanner, email), new LocalUserDao().get(email));
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
