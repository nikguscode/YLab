package core;

import infrastructure.dao.user.LocalUserDao;
import core.entity.Habit;
import core.entity.User;
import core.enumiration.Frequency;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class HabitMarkChecker {
//    public static void checkAllMarks(String email) {
//        User user = new LocalUserDao().get(email);
//        Map<Long, Habit> habits = user.getHabits();
//        habits.values().forEach(e -> {
//            Duration difference = Duration.between(e.getShiftedDateAndTime(), LocalDateTime.now());
//            if (difference.toMinutes() >= Frequency.convertToInteger(e.getFrequency()) * 1440L) {
//                e.setCompleted(false);
//            }
//        });
//    }
}