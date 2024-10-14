package adapters.controller.habit;

import adapters.console.Constants;
import core.HabitMarkService;
import core.LocalDateTimeFormatter;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitIdException;
import core.exceptions.InvalidHabitInformationException;
import core.entity.Habit;
import core.entity.User;
import usecase.habit.HabitStreakService;
import usecase.habit.MarkDateShifter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class HabitSettingsController {
    public void handle(Scanner scanner, User user, String input) throws InterruptedException, InvalidFrequencyConversionException, InvalidHabitIdException {
        HabitMarkService.checkAllMarks(user);
        Habit habit = getSelectedHabit(input, user.getHabits());

        while (true) {
            String currentInput = printHabitMenu(scanner, input, habit);
            List<LocalDateTime> history = habit.getHistory();

            switch (currentInput) {
                case "1", "1.", "Отметить выполнение", "1. Отметить выполнение":
                    markHabit(habit, history);
                    return;
                case "2", "2.", "Редактировать", "2. Редактировать":
                    try {
                        new HabitEditController().handle(scanner, user, habit);
                    } catch (InvalidHabitInformationException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3", "3.", "Посмотреть историю выполнения", "3. Посмотреть историю выполнения":
                    System.out.println("История выполнения привычки:");
                    history.forEach(e -> System.out.println(e.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));
                    System.out.println("----------------------------");
                    Thread.sleep(800);
                    break;
                case "4", "4.", "Сгенерировать статистику", "4. Сгенерировать статистику":
                    new HabitStatisticsController().handle(scanner, habit);
                    break;
                case "5", "5.", "Подробная информация", "5. Подробная информация":
                    String habitInformationInput = printHabitInformation(habit, scanner);

                    while (true) {
                        switch (habitInformationInput) {
                            case "0", "0.", "Вернуться назад", "0. Вернуться назад" -> {
                                return;
                            }
                            default -> System.out.println("Указана некоректная опция");
                        }
                    }
                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                    return;
                default:
                    System.out.println("Указана некорректная опция!");
            }
        }
    }

    private Habit getSelectedHabit(String input, Map<Long, Habit> habits) throws InvalidHabitIdException {
        long habitId = getHabitIdFromUserInput(input, habits);
        return habits.get(habitId);
    }

    private String printHabitMenu(Scanner scanner, String input, Habit habit) throws InvalidFrequencyConversionException {
        System.out.println(Constants.SELECTED_HABIT_SETTINGS);

        new HabitStreakService().getCurrentStreak(habit);
        System.out.printf(
                "Идентификатор: %s | Название: %s | Статус: %s | Частота: %s | Streak: %s\n",
                input,
                habit.getTitle(),
                habit.isCompleted() ? "Выполнена" : "Не выполнена",
                habit.getFrequency().getValue(),
                habit.getStreak()
        );

        System.out.print(Constants.HABIT_SETTINGS_MENU);
        return scanner.nextLine();
    }

    private void markHabit(Habit habit, List<LocalDateTime> history) throws InvalidFrequencyConversionException {
        if (habit.isCompleted()) {
            System.out.println("Ошибка, привычка уже выполнена, ожидайте её сброса!");
            return;
        }

        habit.setCompleted(true);
        new MarkDateShifter().shiftMarkDate(habit);
        HabitMarkService.unmarkedHabits--;
        System.out.println("Привычка отмечена как выполненная");
        history.add(LocalDateTime.now());
    }

    private String printHabitInformation(Habit habit, Scanner scanner) {
        System.out.println("-----------------------------------");
        System.out.println("# | Идентификатор: " + habit.getId());
        System.out.println("# | Название: " + habit.getTitle());
        System.out.println("# | Описание: " + habit.getDescription());
        System.out.println("# | Статус: " + (habit.isCompleted() ? "Выполнена" : "Не выполнена"));
        System.out.println("# | Дата создания: " + LocalDateTimeFormatter.format(habit.getCreationDateAndTime()));
        System.out.println("# | Дата последней отметки: " +
                Optional.ofNullable(habit.getLastMarkDateAndTime())
                        .map(LocalDateTimeFormatter::format)
                        .orElse("отметок ещё не было"));
        System.out.println("# | Дата ближайшей отметки: " + LocalDateTimeFormatter.format(habit.getShiftedDateAndTime()));
        System.out.println("# | Частота: " + habit.getFrequency().getValue());
        System.out.println("0. Вернуться назад");
        System.out.println("-----------------------------------");
        System.out.print("Выберите опцию: ");
        return scanner.nextLine();
    }

    private long getHabitIdFromUserInput(String input, Map<Long, Habit> habits) throws InvalidHabitIdException {
        long currentId;

        try {
            currentId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный идентификатор");
            throw new InvalidHabitIdException();
        }

        if (!habits.containsKey(currentId)) {
            System.out.println("Указаного идентификатора не существует!");
            throw new InvalidHabitIdException();
        }

        return currentId;
    }
}