package usecase.habit;

import core.entity.Habit;
import core.entity.User;
import core.enumiration.Frequency;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dao.HabitMarkHistory.HabitMarkHistoryDao;
import infrastructure.dao.HabitMarkHistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.user.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HabitMarkServiceTest {

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private DatabaseUtils databaseUtils;
    private UserDao userDao;
    private User user;
    private HabitDao habitDao;
    private HabitMarkHistoryDao habitMarkHistoryDao;
    private Habit habit;

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
    public void beforeEach() {
        this.databaseUtils = new DatabaseUtils(
                "org.postgresql.Driver",
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        this.habitMarkHistoryDao = new JdbcHabitMarkHistoryDao(databaseUtils);
        this.habitDao = new JdbcHabitDao(databaseUtils, habitMarkHistoryDao);
    }

    // проверяем: статус, дату последней отметки, дату следующей отметки, историю отметок
    @Test
    @DisplayName("Habit Marked Successfully")
    public void test_Habit_Marked_Successfully() throws InvalidHabitInformationException, InvalidFrequencyConversionException {
        this.habit = Habit.builder()
                .id(0)
                .title("test")
                .description("test")
                .isCompleted(false)
                .creationDateAndTime(LocalDateTime.now())
                .frequency(Frequency.EVERY_DAY)
                .build();
        new HabitMarkService(habitDao, habitMarkHistoryDao).mark(habit);
        Assertions.assertThat(habit.isCompleted()).isTrue();
        Assertions.assertThat(habit.getLastMarkDateAndTime().toLocalDate()).isEqualTo(LocalDate.now());
        Assertions.assertThat(habit.getNextMarkDateAndTime()).isEqualTo(
                LocalDateTime.of(
                        LocalDate.now().plusDays(habit.getFrequency().getIntegerValue()),
                        habit.getCreationDateAndTime().toLocalTime()
                ));
        Assertions.assertThat(habitMarkHistoryDao.getAll(habit)).isNotNull();
    }

    @Test
    @DisplayName("Habit Already Marked")
    public void test_Already_Marked() throws InvalidHabitInformationException, InvalidFrequencyConversionException {
        this.habit = Habit.builder()
                .id(0)
                .title("test")
                .description("test")
                .isCompleted(true)
                .creationDateAndTime(LocalDateTime.now())
                .frequency(Frequency.EVERY_DAY)
                .build();
        new HabitMarkService(habitDao, habitMarkHistoryDao).mark(habit);
        Assertions.assertThat(habit.isCompleted()).isTrue();
        Assertions.assertThat(habit.getLastMarkDateAndTime()).isNull();
        Assertions.assertThat(habit.getNextMarkDateAndTime()).isNull();
    }

    @Test
    @DisplayName("Marking Before Next Mark Date")
    public void test_Marking_Before_Next_Mark_Date() throws InvalidHabitInformationException, InvalidFrequencyConversionException {
        this.habit = Habit.builder()
                .id(0)
                .title("test")
                .description("test")
                .isCompleted(true)
                .creationDateAndTime(LocalDateTime.now())
                .nextMarkDateAndTime(LocalDateTime.of(
                        LocalDate.of(2099, 1, 1),
                        LocalTime.now()
                ))
                .frequency(Frequency.EVERY_DAY)
                .build();
        new HabitMarkService(habitDao, habitMarkHistoryDao).mark(habit);
        Assertions.assertThat(habit.isCompleted()).isTrue();
        Assertions.assertThat(habit.getLastMarkDateAndTime()).isNull();
    }
}