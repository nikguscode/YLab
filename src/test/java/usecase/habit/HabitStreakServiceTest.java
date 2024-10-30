package usecase.habit;

import core.entity.Habit;
import core.entity.User;
import common.enumiration.Frequency;
import common.enumiration.Role;
import core.exceptions.usecase.InvalidFrequencyConversionException;
import core.exceptions.usecase.InvalidHabitInformationException;
import core.exceptions.usecase.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dao.habitmarkhistory.HabitMarkHistoryDao;
import infrastructure.dao.habitmarkhistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.user.JdbcUserDao;
import infrastructure.dao.user.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;


public class HabitStreakServiceTest {
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private UserDao userDao;
    private HabitDao habitDao;
    private HabitMarkHistoryDao habitMarkHistoryDao;
    private User user;
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
    public void setUp() throws InvalidUserInformationException, InvalidHabitInformationException {
        DatabaseUtils databaseUtils = new DatabaseUtils(
                "org.postgresql.Driver",
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        this.userDao = new JdbcUserDao(databaseUtils);
        this.habitMarkHistoryDao = new JdbcHabitMarkHistoryDao(databaseUtils);
        this.habitDao = new JdbcHabitDao(databaseUtils, habitMarkHistoryDao);

        // создаём пользователя для каждого теста
        this.user = User.builder()
                .id(0)
                .email("test@gmail.com")
                .username("test")
                .password("test")
                .role(Role.USER)
                .isAuthorized(true)
                .registrationDate(LocalDateTime.now())
                .authorizationDate(LocalDateTime.now())
                .build();
        userDao.add(this.user);

        // создаём одну привычку для каждого теста
        this.habit = Habit.builder()
                .id(0)
                .title("test")
                .description("test")
                .frequency(Frequency.DAILY)
                .creationDateAndTime(LocalDateTime.of(LocalDate.of(2023, 1, 1), LocalTime.now()))
                .build();
        habitDao.add(habit);

        if (this.habit.getHistory() == null) {
            this.habit.setHistory(new ArrayList<>());
        }
    }

    @AfterEach
    public void afterEach() {
        userDao.delete(this.user);
        habitDao.delete(this.habit);
        this.user = null;
        this.habit = null;
    }

    // currentDate - последная отметка, соответсвующая текущей дате отметки
    // previousDate1 - предыдущая дата отметки относительно currentDate
    // previousDate2 - предыдущая дата отметки относительно previousDate1
    // previousDate3 - предыдущая дата отметки относительно previousDate2
    @Test
    @DisplayName("Streak Exists")
    public void test_Streak_Exists() throws InvalidFrequencyConversionException {
        long habitId = this.habit.getId();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate1 = currentDate.minusDays(1);
        LocalDateTime previousDate2 = currentDate.minusDays(2).minusHours(2);
        LocalDateTime previousDate3 = currentDate.minusDays(3).minusHours(4);
        // данная отметка НЕ должна быть включена в серию
        LocalDateTime previousDate4 = currentDate.minusDays(5).minusHours(4).minusMinutes(2);

        System.out.println("Дата и время на которой должна сбиться серия: " + previousDate4);
        System.out.println("Дата и время с которой должна начаться серия:" + previousDate3);

        habitMarkHistoryDao.add(habit, previousDate4);
        habitMarkHistoryDao.add(habit, previousDate3);
        habitMarkHistoryDao.add(habit, previousDate2);
        habitMarkHistoryDao.add(habit, previousDate1);
        habitMarkHistoryDao.add(habit, currentDate);

        int currentStreak = new HabitStreakService(habitMarkHistoryDao).getCurrentStreak(habit);
        Assertions.assertThat(currentStreak).isEqualTo(4);
    }

    // в истории отметок есть серия, но эта серия была сбита последней отметкой
    @Test
    @DisplayName("Streak Exists In History But Knocked Down At Last Mark")
    public void test_Streak_Exists_In_History_But_Knocked_Down_At_Last_Mark() throws InvalidFrequencyConversionException {
        long habitId = this.habit.getId();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime previousDate1 = currentDate.minusDays(3);
        LocalDateTime previousDate2 = currentDate.minusDays(4);
        LocalDateTime previousDate3 = currentDate.minusDays(5);
        // данная отметка НЕ должна быть включена в серию

        System.out.println("Дата и время на которой должна сбиться серия: " + currentDate);
        System.out.println("Дата и время с которой должна начаться серия:" + previousDate1);

        habitMarkHistoryDao.add(habit, previousDate3);
        habitMarkHistoryDao.add(habit, previousDate2);
        habitMarkHistoryDao.add(habit, previousDate1);
        habitMarkHistoryDao.add(habit, currentDate);

        int currentStreak = new HabitStreakService(habitMarkHistoryDao).getCurrentStreak(habit);
        Assertions.assertThat(currentStreak).isEqualTo(1);
    }
}