package core;

import core.entity.Habit;
import core.entity.User;
import core.enumiration.Frequency;
import core.exceptions.InvalidHabitInformationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ExpiredHabitMarkServiceTest {
    private final User user = User.builder()
            .habits(new HashMap<>())
            .build();
    private final Map<Long, Habit> habitMap = user.getHabits();

    @Test
    @DisplayName("Время отметки превышает 24 часа")
    public void marking_Is_Overdue_For_24_Hours_With_Daily_Marking() throws InvalidHabitInformationException {
        Habit habit = Habit.builder()
                .title("1")
                .description("1")
                .frequency(Frequency.EVERY_DAY)
                .creationDateAndTime(LocalDateTime.now())
                .isCompleted(true)
                .nextMarkDateAndTime(LocalDateTime.of(
                        LocalDate.now().minusDays(1), LocalTime.now() // ставим дату, когда должна была быть отметка на 24 часа назад
                ))
                .build();
        habitMap.put(1L, habit);
        ExpiredHabitMarkService.checkAllMarks(user);
    }
}
