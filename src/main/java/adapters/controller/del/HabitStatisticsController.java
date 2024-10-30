//package adapters.controller.habit;
//
//import core.entity.Habit;
//import core.exceptions.usecase.InvalidFrequencyConversionException;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.Year;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Scanner;
//
//public class HabitStatisticsController {
//    public void handle(Scanner scanner, Habit currentHabit) throws InvalidFrequencyConversionException {
//        while (true) {
//            System.out.print(Constants.HABIT_STATISTICS_MENU);
//            String input = scanner.nextLine();
//            List<LocalDateTime> history = currentHabit.getHistory();
//
//            if (input.matches("\\d{2}:\\d{2} \\d{2}/\\d{2}/\\d{4}")) {
//                LocalDateTime inputDate = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
//                if (inputDate.isAfter(LocalDateTime.now())) {
//                    System.out.println("Невозможно сформировать статистику на будущую дату!");
//                    return;
//                }
//
//                if (history.contains(inputDate)) {
//                    System.out.println("Выполнена");
//                    return;
//                }
//
//                Duration duration = Duration.between(currentHabit.getCreationDateAndTime(), inputDate);
//                long intDuration = duration.toDays();
//                int intFrequency = currentHabit.getFrequency().getIntegerValue();
//                if (Year.of(inputDate.getYear()).isLeap()) {
//                    intDuration--;
//                }
//                if (intDuration >= intFrequency) {
//                    if (intDuration % intFrequency == 0) {
//                        if (inputDate.toLocalTime().isAfter(currentHabit.getCreationDateAndTime().toLocalTime()) ||
//                                inputDate.toLocalTime().equals(currentHabit.getCreationDateAndTime().toLocalTime())) {
//                            System.out.println("Не выполнена");
//                            return;
//                        }
//                    }
//                }
//
//                System.out.println("Не требуется");
//            } else if (input.matches("^неделя=\\d+$")) {
//
//            } else if (input.matches("^месяц=\\d+$")) {
//
//            } else if (input.equals("0") || input.equals("0.") || input.equals("Вернуться назад") || input.equals("0. Вернуться назад")) {
//                return;
//            } else {
//                System.out.println("Некорректный ввод!");
//            }
//        }
//    }
//}