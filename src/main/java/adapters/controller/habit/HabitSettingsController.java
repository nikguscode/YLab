package adapters.controller.habit;

import adapters.console.Constants;
import core.HabitMarkService;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitIdException;
import core.exceptions.InvalidHabitInformationException;
import core.entity.Habit;
import core.entity.User;
import usecase.habit.HabitStreakService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HabitSettingsController {
    public void handle(Scanner scanner, User user, String input) throws InterruptedException, InvalidFrequencyConversionException, InvalidHabitIdException {
        HabitMarkService.checkAllMarks(user);
        Map<Long, Habit> habits = user.getHabits();
        long habitId = getHabitIdFromUserInput(input, habits);
        Habit habit = habits.get(habitId);

        while (true) {
            System.out.println(Constants.SELECTED_HABIT_SETTINGS);
            new HabitStreakService().getCurrentStreak(habits.get(habitId));
            System.out.printf(
                    "Идентификатор: %s | Название: %s | Статус: %s | Частота: %s | Streak: %s\n",
                    input,
                    habit.getTitle(),
                    habit.isCompleted() ? "Выполнена" : "Не выполнена",
                    habit.getFrequency().getValue(),
                    habit.getStreak()
            );

            System.out.println("1. Отметить выполнение");
            System.out.println("2. Редактировать");
            System.out.println("3. Посмотреть историю выполнения");
            System.out.println("4. Сгенерировать статистику");
            System.out.println("5. Подробная информация");
            System.out.println("0. Вернуться назад");
            System.out.print("Укажите опцию: ");
            String currentInput = scanner.nextLine();
            List<LocalDateTime> history = user.getHabits().get(habitId).getHistory();
            Habit currentHabit = user.getHabits().get(habitId);

            switch (currentInput) {
                case "1", "1.", "Отметить выполнение", "1. Отметить выполнение":
                    if (user.getHabits().get(habitId).isCompleted()) {
                        System.out.println("Ошибка, привычка уже выполнена, ожидайте её сброса!");
                        return;
                    }
                    currentHabit.setCompleted(true);
                    currentHabit.setShiftedDateAndTime(
                            LocalDateTime.of(LocalDate.now(), currentHabit.getCreationDateAndTime().toLocalTime())
                    );
                    HabitMarkService.unmarkedHabits--;
                    System.out.println("Привычка отмечена как выполненная");
                    history.add(LocalDateTime.now());
                    return;
                case "2", "2.", "Редактировать", "2. Редактировать":
                    try {
                        new HabitEditController().handle(scanner, user, habits.get(habitId));
                    } catch (InvalidHabitInformationException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3", "3.", "Посмотреть историю выполнения", "3. Посмотреть историю выполнения":
                    history.forEach(e -> System.out.println(e.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));
                    break;
                case "4", "4.", "Сгенерировать статистику", "4. Сгенерировать статистику":
                    new HabitStatisticsController().handle(scanner, currentHabit);
                    break;
                case "5", "5.", "Подробная информация", "5. Подробная информация":
                    System.out.println("Идентификатор: " + currentHabit.getId());
                    System.out.println("Название: " + currentHabit.getTitle());
                    System.out.println("Описание: " + currentHabit.getDescription());
                    System.out.println("Статус: " + (currentHabit.isCompleted() ? "Выполнена" : "Не выполнена"));
                    System.out.println("Дата создания: " + currentHabit.getCreationDateAndTime());
                    System.out.println("Дата последней отметки: " + currentHabit.getLastMarkDateAndTime());
                    System.out.println("Дата ближайшей отметки: " + currentHabit.getShiftedDateAndTime());
                    System.out.println("Частота: " + currentHabit.getFrequency().getValue());
                    System.out.println("0. Вернуться назад");
                    System.out.print("> ");
                    String temp = scanner.nextLine();
                    switch (temp) {
                        case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                            return;
                        default:
                            System.out.println("Указана некорректная опция!");
                    }
                    break;
                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
                    return;
                default:
                    System.out.println("Указана неверная опция");
            }
        }
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

    private void printHabitMenu() {

    }
}