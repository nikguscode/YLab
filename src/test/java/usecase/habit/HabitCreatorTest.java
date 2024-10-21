package usecase.habit;

import core.entity.User;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dto.HabitDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

public class HabitCreatorTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    public final HabitCreator habitCreator = new HabitCreator();
    private User user;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
        new LiquibaseMigration(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        ).migrateDatabase();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    public void setUp() throws InvalidUserInformationException {
        user = User.builder()
                .email("test@gmail.com")
                .username("test")
                .password("test")
                .build();
        user.setId(1);

        DatabaseUtils databaseUtils = new DatabaseUtils(
                "org.postgresql.Driver",
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

    }

    @AfterEach
    public void afterEach() {
        if (user != null) {
            user = null;
        }
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