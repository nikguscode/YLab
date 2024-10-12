package adapters.out;

import adapters.console.Constants;
import core.entity.Habit;
import core.entity.User;

import java.util.Comparator;
import java.util.function.Predicate;

public class HabitListOut {
    public void outList(User user, Predicate<? super Habit> predicate, Comparator<? super  Habit> comparator) {
        System.out.println(Constants.HABIT_LIST);
        if (predicate == null && comparator == null) {
            filter(user);
        } else if (predicate != null){
            filter(user, predicate);
        } else {
            sort(user, comparator);
        }
        System.out.print(Constants.FILTER_FOOTER);
    }

    private void sort(User user, Comparator<? super Habit> comparator) {
        user.getHabits().values().stream()
                .sorted(comparator)
                .forEach(
                        e -> {
                            String currentHabit = String.format(
                                    "Идентификатор: %s | Название: %s | Статус: %s | Частота: %s",
                                    e.getId(),
                                    e.getTitle(),
                                    e.isCompleted() ? "Выполнена" : "Не выполнена",
                                    e.getFrequency().getValue()
                            );
                            System.out.println(currentHabit);
                        });
    }

    private void filter(User user) {
        user.getHabits().values()
                .forEach(e -> {
                    String currentHabit = String.format(
                            "Идентификатор: %s | Название: %s | Статус: %s | Частота: %s",
                            e.getId(),
                            e.getTitle(),
                            e.isCompleted() ? "Выполнена" : "Не выполнена",
                            e.getFrequency().getValue()
                    );
                    System.out.println(currentHabit);
                });
    }

    private void filter(User user, Predicate<? super Habit> predicate) {
        user.getHabits().values().stream()
                .filter(predicate)
                .forEach(e -> {
                    String currentHabit = String.format(
                            "Идентификатор: %s | Название: %s | Статус: %s | Частота: %s",
                            e.getId(),
                            e.getTitle(),
                            e.isCompleted() ? "Выполнена" : "Не выполнена",
                            e.getFrequency().getValue()
                    );
                    System.out.println(currentHabit);
                });
    }
}