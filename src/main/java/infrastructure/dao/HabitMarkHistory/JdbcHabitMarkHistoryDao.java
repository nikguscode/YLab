package infrastructure.dao.HabitMarkHistory;

import core.entity.Habit;
import infrastructure.DatabaseUtils;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JDBC реализация взаимодействия с базой данных, содержащая только CRUD операции
 */
@RequiredArgsConstructor
public class JdbcHabitMarkHistoryDao implements HabitMarkHistoryDao {
    /**
     * Экземпляр вспомогательного класса для взаимодействия с базой данных
     */
    private final DatabaseUtils databaseUtils;

    /**
     * Добавляет отметку через идентификатор привычки
     * @param habitId идентификатор привычки
     */
    @Override
    public void add(long habitId, LocalDateTime dateTime) {
        String sqlQuery = "INSERT INTO entity.habit_mark_history" +
                "(habit_id, mark_date) " +
                "VALUES (?, ?)";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habitId);
            preparedStatement.setTimestamp(2, dateTime != null
                    ? Timestamp.valueOf(dateTime)
                    : Timestamp.valueOf(LocalDateTime.now()));

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет отметку через сущность привычки
     * @param habit привычка, для которой ставится отметка
     */
    @Override
    public void add(Habit habit, LocalDateTime dateTime) {
        String sqlQuery = "INSERT INTO entity.habit_mark_history" +
                "(habit_id, mark_date) " +
                "VALUES (?, ?)";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habit.getId());
            preparedStatement.setTimestamp(2, dateTime != null
                    ? Timestamp.valueOf(dateTime)
                    : Timestamp.valueOf(LocalDateTime.now()));

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получает все даты отметок для привычки
     * @param habitId идентификатор привычки для которой необходимо получить даты
     * @return список, содержащий даты отметок
     */
    @Override
    public List<LocalDateTime> getAll(long habitId) {
        String sqlQuery = "SELECT * FROM entity.habit_mark_history WHERE habit_id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habitId);
            List<Map<String, Object>> rows = databaseUtils.map(preparedStatement.executeQuery());
            List<LocalDateTime> markHistory = new ArrayList<>();

            for (Map<String, Object> currentRow : rows) {
                markHistory.add(((Timestamp) currentRow.get("mark_date")).toLocalDateTime());
            }

            return markHistory;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Получает все даты отметок для привычки
     * @param habit привычка для которой необходимо получить даты
     * @return список, содержащий даты отметок
     */
    @Override
    public List<LocalDateTime> getAll(Habit habit) {
        String sqlQuery = "SELECT * FROM entity.habit_mark_history WHERE habit_id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habit.getId());
            List<Map<String, Object>> rows = databaseUtils.map(preparedStatement.executeQuery());
            List<LocalDateTime> markHistory = new ArrayList<>();

            for (Map<String, Object> currentRow : rows) {
                markHistory.add(((Timestamp) currentRow.get("mark_date")).toLocalDateTime());
            }

            return markHistory;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Очищает историю отметок для указанной привычки
     * @param habit привычка, для которой выполняется очистка
     */
    @Override
    public void clear(Habit habit) {
        String sqlQuery = "DELETE FROM entity.habit_mark_history WHERE habit_id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habit.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
