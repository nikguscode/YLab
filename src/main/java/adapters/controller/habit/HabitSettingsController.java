package adapters.controller.habit;

import adapters.console.Constants;
import core.exceptions.InvalidFrequencyConversionException;
import infrastructure.dao.user.LocalUserDao;
import core.entity.Habit;
import core.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HabitSettingsController {
    public void handle(Scanner scanner, String email, String input) throws InterruptedException, InvalidFrequencyConversionException {
        User user = new LocalUserDao().get(email);
        Map<Long, Habit> habits = user.getHabits();
        long currentId;

        try {
            currentId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный идентификатор");
            return;
        }

        if (!habits.containsKey(currentId)) {
            System.out.println("Указаного идентификатора не существует!");
            return;
        }

        while (true) {
            System.out.println(Constants.SELECTED_HABIT_SETTINGS);
            System.out.printf(
                    "Идентификатор: %s | Название: %s | Статус: %s | Частота: %s\n",
                    input,
                    habits.get(currentId).getTitle(),
                    habits.get(currentId).isCompleted() ? "Выполнена" : "Не выполнена",
                    habits.get(currentId).getFrequency().getValue()
            );

            System.out.println("1. Отметить выполнение");
            System.out.println("2. Редактировать");
            System.out.println("3. Посмотреть историю выполнения");
            System.out.println("4. Сгенерировать статистику");
            System.out.println("5. Подробная информация");
            System.out.println("0. Вернуться назад");
            System.out.print("Укажите опцию: ");
            String currentInput = scanner.nextLine();
            List<LocalDateTime> history = user.getHabits().get(currentId).getHistory();
            Habit currentHabit = user.getHabits().get(currentId);

            switch (currentInput) {
                case "1", "1.", "Отметить выполнение", "1. Отметить выполнение":
                    if (user.getHabits().get(currentId).isCompleted()) {
                        System.out.println("Ошибка, привычка уже выполнена, ожидайте её сброса!");
                        return;
                    }
                    currentHabit.setCompleted(true);
                    currentHabit.setShiftedDateAndTime(
                            LocalDateTime.of(LocalDate.now(), currentHabit.getCreationDateAndTime().toLocalTime())
                    );
                    System.out.println("Привычка отмечена как выполненная");
                    history.add(LocalDateTime.now());
                    return;
                case "2", "2.", "Редактировать", "2. Редактировать":
                    new HabitEditController().handle(scanner, email, habits.get(currentId));
                    break;
                case "3", "3.", "Посмотреть историю выполнения", "3. Посмотреть историю выполнения":
                    history.forEach(e -> System.out.println(e.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));
                    break;
                case "4", "4.", "Сгенерировать статистику", "4. Сгенерировать статистику":
                    new HabitStatisticsController().handle(scanner, email, currentHabit);
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
}