package usecase.habit;

import core.entity.Habit;
import core.entity.User;
import common.enumiration.Frequency;
import core.exceptions.usecase.InvalidFrequencyConversionException;
import core.exceptions.usecase.InvalidHabitInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dao.habitmarkhistory.HabitMarkHistoryDao;
import infrastructure.dao.habitmarkhistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.user.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MarkDateShifterTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
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
        DatabaseUtils databaseUtils = new DatabaseUtils(
                "org.postgresql.Driver",
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        this.habitMarkHistoryDao = new JdbcHabitMarkHistoryDao(databaseUtils);
        this.habitDao = new JdbcHabitDao(databaseUtils, habitMarkHistoryDao);
    }

    // сдвиг даты отметки после отметки
    @Test
    @DisplayName("Shift Date For Mark After Marking")
    public void shift_Date_After_Mark() throws InvalidHabitInformationException, InvalidFrequencyConversionException {
        this.habit = Habit.builder()
                .id(0)
                .title("test")
                .description("test")
                .isCompleted(false)
                .creationDateAndTime(LocalDateTime.now())
                .frequency(Frequency.DAILY)
                .build();

        habitDao.add(habit);
        new HabitMarkService(habitDao, habitMarkHistoryDao).mark(habit);
        Assertions.assertThat(habit.getNextMarkDateAndTime().toLocalDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    @DisplayName("Shift Date For Mark After Marking Monthly Frequency")
    public void shift_Date_After_Mark_Monthly_Frequency() throws InvalidHabitInformationException, InvalidFrequencyConversionException {
        this.habit = Habit.builder()
                .id(0)
                .title("test")
                .description("test")
                .isCompleted(false)
                .creationDateAndTime(LocalDateTime.now())
                .frequency(Frequency.MONTHLY)
                .build();

        habitDao.add(habit);
        new HabitMarkService(habitDao, habitMarkHistoryDao).mark(habit);
        Assertions.assertThat(habit.getNextMarkDateAndTime().toLocalDate()).isEqualTo(LocalDate.now().plusDays(30));
    }

    // дата следующей отметки существует, текущая дата позже чем дата отметки
    @Test
    @DisplayName("Shift Date If Next Date Mark Already Exists")
    public void shift_Date_If_Next_Date_Mark_Already_Exists() throws InvalidHabitInformationException, InvalidFrequencyConversionException {
        this.habit = Habit.builder()
                .id(0)
                .title("test")
                .description("test")
                .isCompleted(false)
                .creationDateAndTime(LocalDateTime.now())
                .nextMarkDateAndTime(LocalDateTime.of(
                        LocalDate.of(2024, 1, 1),
                        LocalTime.now()
                ))
                .frequency(Frequency.DAILY)
                .build();

        habitDao.add(habit);
        new HabitMarkService(habitDao, habitMarkHistoryDao).mark(habit);
        Assertions.assertThat(habit.getNextMarkDateAndTime().toLocalDate()).isEqualTo(LocalDate.now().plusDays(1));
    }
}
