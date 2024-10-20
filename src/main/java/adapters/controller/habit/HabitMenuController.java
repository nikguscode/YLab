package adapters.controller.habit;

import adapters.console.Constants;
import adapters.in.HabitCreationInput;
import core.HabitMarkService;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import infrastructure.dao.HabitMarkHistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dto.HabitDto;
import lombok.RequiredArgsConstructor;
import usecase.habit.HabitCreator;
import usecase.habit.MarkDateShifter;

import java.util.Scanner;

/**
 * Главный контроллер, отвечающий за вызов других контроллеров, относящихся к {@link Habit}
 */
@RequiredArgsConstructor
public class HabitMenuController {
    private final HabitDao habitDao;

    public void handle(Scanner scanner, User user) throws InvalidFrequencyConversionException {
        while (true) {
            HabitMarkService.checkAllMarks(user);
            System.out.print(Constants.HABIT_MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "Список привычек", "1. Список привычек":
                    new HabitListController(habitDao).handle(scanner, user);
                    break;
                case "2", "2.", "Добавить привычку", "2. Добавить привычку":
                    try {
                        HabitDto habitDto = new HabitCreationInput().input(scanner);
                        Habit habit = new HabitCreator().create(user, habitDto);

                        if (habit.getNextMarkDateAndTime() == null) {
                            new MarkDateShifter().shiftMarkDate(habit);
                        }

                        long habitId = habitDao.add(habit);
                        user.setHabits(habitDao.getAll(user));
                        new JdbcHabitMarkHistoryDao().add(habitId);
                        System.out.println("Добавление привычки...");
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
}