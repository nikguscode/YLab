package adapters.controller.habit;

import adapters.console.Constants;
import core.ExpiredHabitMarkService;
import core.LocalDateTimeFormatter;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitIdException;
import core.exceptions.InvalidHabitInformationException;
import infrastructure.dao.HabitMarkHistory.HabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import lombok.RequiredArgsConstructor;
import usecase.habit.HabitMarkService;
import usecase.habit.HabitStreakService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Контроллер, отвечающий за взаимодействие с конкретной привычкой, выбранной из {@link HabitListController}
 */
@RequiredArgsConstructor
public class HabitSettingsController {
    private final HabitDao habitDao;
    private final HabitMarkHistoryDao habitMarkHistoryDao;

    public void handle(Scanner scanner, User user, String input) throws InvalidFrequencyConversionException, InvalidHabitIdException {
        ExpiredHabitMarkService.checkAllMarks(user);
        Habit habit = getSelectedHabit(input, user.getHabits());

        while (true) {
            String currentInput = printHabitMenu(scanner, habit);
            List<LocalDateTime> history = habit.getHistory();

            switch (currentInput) {
                case "1", "1.", "Отметить выполнение", "1. Отметить выполнение":
                    new HabitMarkService(habitDao, habitMarkHistoryDao).mark(habit);
                    return;
                case "2", "2.", "Редактировать", "2. Редактировать":
                    try {
                        new HabitEditController(habitDao).handle(scanner, user, habit);
                    } catch (InvalidHabitInformationException e) {
                        throw new RuntimeException(e);
                    }

                    user.setHabits(habitDao.getAll(user));
                    if (getSelectedHabit(input, user.getHabits()) == null) {
                        return;
                    }
                    break;
                case "3", "3.", "Посмотреть историю выполнения", "3. Посмотреть историю выполнения":
                    System.out.println("История выполнения привычки:");
                    history.forEach(e -> System.out.println(e.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));
                    System.out.println("----------------------------");
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

    /**
     * Получаем, выбранную привычку из базы данных
     *
     * @param input  идентификатор привычки, используется в {@link HabitSettingsController#getHabitIdFromUserInput(String, Map)
     *               getHabitIdFromUserInput()} для безопасного перевода идентификатора из строки в число
     * @param habits база данных, содержащая привычки, в следующий версиях будет заменена на DAO интерфейс
     * @return привычку, найденную в базе данных по идентификатору
     * @throws InvalidHabitIdException возникает в том случае, если пользователь указал некорректный идентификатор в
     *                                 {@link HabitListController}
     */
    private Habit getSelectedHabit(String input, Map<Long, Habit> habits) throws InvalidHabitIdException {
        long habitId = getHabitIdFromUserInput(input, habits);
        return habits.get(habitId);
    }

    /**
     * Метод, отвечающий за вывод информации о выбранной привычки и меню для взаимодействия с ней, также считывает
     * пользовательский ввод
     *
     * @param scanner экземпляр сканер
     * @param habit   привычка, которую необходимо вывести
     * @return вывод для консоли
     * @throws InvalidFrequencyConversionException возникает при некорректном преобразовании {@link core.enumiration.Frequency
     *                                             Frequency} в другой тип данных
     */
    private String printHabitMenu(Scanner scanner, Habit habit) throws InvalidFrequencyConversionException {
        System.out.println(Constants.SELECTED_HABIT_SETTINGS);

        new HabitStreakService(habitMarkHistoryDao).getCurrentStreak(habit);
        System.out.printf(
                "Идентификатор: %s | Название: %s | Статус: %s | Частота: %s | Streak: %s\n",
                habit.getId(),
                habit.getTitle(),
                habit.isCompleted() ? "Выполнена" : "Не выполнена",
                habit.getFrequency().getStringValue(),
                habit.getStreak()
        );

        System.out.print(Constants.HABIT_SETTINGS_MENU);
        return scanner.nextLine();
    }

    /**
     * Метод, который отвечает за вывод подробной информации о привычке
     * @param habit привычка для котрой необходимо вывести информацию
     * @param scanner экземпляр сканера
     * @return вывод для консоли
     */
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
        System.out.println("# | Дата ближайшей отметки: " + LocalDateTimeFormatter.format(habit.getNextMarkDateAndTime()));
        System.out.println("# | Частота: " + habit.getFrequency().getStringValue());
        System.out.println("0. Вернуться назад");
        System.out.println("-----------------------------------");
        System.out.print("Выберите опцию: ");
        return scanner.nextLine();
    }

    /**
     * Метод, отвечающий за обработку и преборазование пользовательского ввода в идентификатор привычки
     * @param input пользоватеский ввод
     * @param habits база данных привычек, в следующей версии будет заменена на DAO Интерфейс
     * @return идентификатор в числовом представлении
     * @throws InvalidHabitIdException возникает в том случае, если пользователь указал некорректный идентификатор в
     * {@link HabitListController}
     */
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