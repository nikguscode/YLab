//package adapters.controller.habit;
//
//import common.enumiration.Frequency;
//import core.exceptions.usecase.InvalidFrequencyConversionException;
//import core.entity.Habit;
//
//import java.util.Scanner;
//import java.util.function.Predicate;
//
///**
// * Контроллер, отвечающий за возврат {@link Predicate} для фильтрации. Не вызывает сервисы при своей работе, из-за простой логики
// */
//public class HabitFilterController {
//    public Predicate<? super Habit> handle(Scanner scanner) {
//        while (true) {
//            System.out.print(Constants.HABIT_FILTER);
//            String input = scanner.nextLine();
//
//            switch (input) {
//                case "1", "1.", "Показать только выполненные", "1. Показать только выполненные":
//                    return Habit::isCompleted;
//                case "2", "2.", "Показать только невыполненные", "2. Показать только невыполненные":
//                    return habit -> !habit.isCompleted();
//                case "3", "3.", "Показать по определённой частоте", "3. Показать по определённой частоте":
//                    System.out.print(Constants.FREQUENCY_LIST);
//                    String inputFrequency = scanner.nextLine();
//                    return habit -> {
//                        try {
//                            return habit.getFrequency() == Frequency.convertFromString(inputFrequency);
//                        } catch (InvalidFrequencyConversionException e) {
//                            throw new RuntimeException(e);
//                        }
//                    };
//                case "0", "0.", "Вернуться назад", "0. Вернуться назад":
//                    return null;
//                default:
//                    System.out.println("Указана некорректная опция!");
//            }
//        }
//    }
//}