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

@RequiredArgsConstructor
public class JdbcHabitMarkHistoryDao implements HabitMarkHistoryDao {
    private final DatabaseUtils databaseUtils;

    public void add(long habitId) {
        String sqlQuery = "INSERT INTO entity.habit_mark_history" +
                "(habit_id, mark_date) " +
                "VALUES (?, ?)";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habitId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void add(Habit habit) {
        String sqlQuery = "INSERT INTO entity.habit_mark_history" +
                "(habit_id, mark_date) " +
                "VALUES (?, ?)";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habit.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public List<LocalDateTime> getAll(Habit habit) {
        String sqlQuery = "SELECT * FROM entity.habit_mark_history WHERE id = ?";

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

    public void clear(Habit habit) {
        String sqlQuery = "DELETE FROM entity.habit_mark_history WHERE id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habit.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
