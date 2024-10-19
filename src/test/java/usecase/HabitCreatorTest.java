package usecase;

import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dto.HabitDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usecase.habit.HabitCreator;

public class HabitCreatorTest {
    public final HabitCreator habitCreator = new HabitCreator();
    public final User user = User.builder()
            .email("test@gmail.com")
            .username("1")
            .password("1")
            .build();

    public HabitCreatorTest() throws InvalidUserInformationException {
    }

    @Test
    @DisplayName("Create Correct Habit")
    public void create_Correct_Habit() throws InvalidFrequencyConversionException, InvalidHabitInformationException {
        HabitDto habitDto = HabitDto.builder()
                .title("test1")
                .description("test2")
                .dateAndTime("")
                .frequency("1. Ежедневно")
                .build();
        habitCreator.create(user, habitDto);
    }

    @Test
    @DisplayName("Create Habit With Incorrect DateTime")
    public void create_Habit_With_Incorrect_DateTime() {
        HabitDto habitDto = HabitDto.builder()
                .title("test1")
                .description("test2")
                .dateAndTime("31/12/2024")
                .frequency("1. Ежедневно")
                .build();
        Assertions.assertThatThrownBy(() -> habitCreator.create(user, habitDto))
                .isInstanceOf(InvalidHabitInformationException.class);
    }

    @Test
    @DisplayName("Create Habit With Incorrect Frequency")
    public void create_Habit_With_Incorrect_Frequency() {
        HabitDto habitDto = HabitDto.builder()
                .title("test1")
                .description("test2")
                .dateAndTime("08:00 31/12/2024")
                .frequency("")
                .build();
        Assertions.assertThatThrownBy(() -> habitCreator.create(user, habitDto))
                .isInstanceOf(InvalidFrequencyConversionException.class);
    }

    @Test
    @DisplayName("Create Habit With Incorrect Title and Description")
    public void create_Habit_With_Incorrect_Title_And_Description() {
        HabitDto habitDto = HabitDto.builder()
                .title("")
                .description("")
                .dateAndTime("08:00 31/12/2024")
                .frequency("1. Ежедневно")
                .build();
        Assertions.assertThatThrownBy(() -> habitCreator.create(user, habitDto))
                .isInstanceOf(InvalidHabitInformationException.class);
    }
}