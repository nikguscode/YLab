package adapters.controller.habit;

import adapters.console.Constants;
import adapters.in.HabitCreationInput;
import core.ExpiredHabitMarkService;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import infrastructure.dao.HabitMarkHistory.HabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dto.HabitDto;
import lombok.RequiredArgsConstructor;
import usecase.habit.HabitCreator;
import usecase.habit.MarkDateShifter;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Главный контроллер, отвечающий за вызов других контроллеров, относящихся к {@link Habit}
 */
@RequiredArgsConstructor
public class HabitMenuController {
    private final HabitDao habitDao;
    private final HabitMarkHistoryDao habitMarkHistoryDao;

    public void handle(Scanner scanner, User user) throws InvalidFrequencyConversionException {
        while (true) {
            ExpiredHabitMarkService.checkAllMarks(user);
            System.out.print(Constants.HABIT_MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "1", "1.", "Список привычек", "1. Список привычек":
                    new HabitListController(habitDao, habitMarkHistoryDao).handle(scanner, user);
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

                        if (habit.isCompleted()) {
                            habitMarkHistoryDao.add(habitId, LocalDateTime.now());
                        }
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