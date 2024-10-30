package infrastructure.dao.habit;

import core.entity.Habit;
import core.entity.User;
import common.enumiration.Frequency;
import core.exceptions.usecase.InvalidHabitInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.habitmarkhistory.HabitMarkHistoryDao;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JDBC реализация взаимодействия с базой данных, содержащая только CRUD операции
 */
@RequiredArgsConstructor
public class JdbcHabitDao implements HabitDao {
    /**
     * Экземпляр вспомогательного класса для взаимодействия с базой данных
     */
    private final DatabaseUtils databaseUtils;

    /**
     * Экземпляр DAO интерфейса для получения истории отметок привычки
     */
    private final HabitMarkHistoryDao habitMarkHistoryDao;

    /**
     * Метод для добавления привычки в базу данных
     * @param habit сущность привычки, которую нужно добавить в базу данных
     */
    @Override
    public long add(Habit habit) {
        String sqlQuery = "INSERT INTO entity.habit" +
                "(user_id, title, description, is_completed, creation_date_and_time, last_mark_date_and_time, next_mark_date_and_time, frequency) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, habit.getUserId());
            preparedStatement.setString(2, habit.getTitle());
            preparedStatement.setString(3, habit.getDescription());
            preparedStatement.setBoolean(4, habit.isCompleted());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(habit.getCreationDateAndTime()));
            preparedStatement.setTimestamp(6, habit.getLastMarkDateAndTime() != null ? Timestamp.valueOf(habit.getLastMarkDateAndTime()) : null);
            preparedStatement.setTimestamp(7, habit.getNextMarkDateAndTime() != null ? Timestamp.valueOf(habit.getNextMarkDateAndTime()) : null);
            preparedStatement.setObject(8, habit.getFrequency(), java.sql.Types.OTHER);

            preparedStatement.executeUpdate();

            try (ResultSet generatedId = preparedStatement.getGeneratedKeys()) {
                if (generatedId.next()) {
                    return generatedId.getLong(1);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Метод для получения всех сущностей привычки из базы данных
     * @return список привычек, где ключом является идентификатор привычки
     */
    @Override
    public Map<Long, Habit> getAll(User user) {
        String sqlQuery = "SELECT * FROM entity.habit WHERE user_id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, user.getId());
            List<Map<String, Object>> rows = databaseUtils.map(preparedStatement.executeQuery());
            Map<Long, Habit> mapOfHabits = new HashMap<>();

            for (Map<String, Object> currentRow : rows) {
                mapOfHabits.put(
                        (long) currentRow.get("id"),
                        Habit.builder()
                                .id((long) currentRow.get("id"))
                                .userId((long) currentRow.get("user_id"))
                                .title((String) currentRow.get("title"))
                                .description((String) currentRow.get("description"))
                                .isCompleted((boolean) currentRow.get("is_completed"))
                                .creationDateAndTime(((Timestamp) currentRow.get("creation_date_and_time")).toLocalDateTime())
                                .lastMarkDateAndTime(Optional.ofNullable(currentRow.get("last_mark_date_and_time"))
                                        .map(e -> ((Timestamp) e).toLocalDateTime())
                                        .orElse(null)
                                )
                                .nextMarkDateAndTime(((Timestamp) currentRow.get("next_mark_date_and_time")).toLocalDateTime())
                                .history(habitMarkHistoryDao.getAll((long) currentRow.get("id")))
                                .frequency(Frequency.valueOf(((String) currentRow.get("frequency")).toUpperCase()))
                                .build()
                );
            }

            return mapOfHabits;
        } catch (SQLException | ClassNotFoundException | InvalidHabitInformationException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Метод для получения сущности привычки из базы данных
     * @param habitId при помощи которого будет выполнен поиск привычки
     * @return сущность привычки
     */
    @Override
    public Habit get(long habitId) {
        String sqlQuery = "SELECT * FROM entity.habit WHERE id=?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habitId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return Habit.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getLong("user_id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .isCompleted(rs.getBoolean("is_completed"))
                        .creationDateAndTime(rs.getTimestamp("creation_date_and_time") != null
                                ? rs.getTimestamp("creation_date_and_time").toLocalDateTime()
                                : null)
                        .lastMarkDateAndTime(rs.getTimestamp("last_mark_date_and_time") != null
                                ? rs.getTimestamp("last_mark_date_and_time").toLocalDateTime()
                                : null)
                        .nextMarkDateAndTime(rs.getTimestamp("next_mark_date_and_time") != null
                                ? rs.getTimestamp("next_mark_date_and_time").toLocalDateTime()
                                : null)
                        .history(habitMarkHistoryDao.getAll(habitId))
                        .frequency(Frequency.valueOf(rs.getString("frequency").toUpperCase()))
                        .build();
            }
        } catch (SQLException | ClassNotFoundException | InvalidHabitInformationException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Метод для изменения привычки в базе данных
     * @param habit отредактированная сущность привычки, которую нужно обновить в базе данных
     */
    @Override
    public void edit(Habit habit) {
        String sqlQuery = "UPDATE entity.habit " +
                "SET user_id=?, " +
                "title=?, " +
                "description=?, " +
                "is_completed=?, " +
                "creation_date_and_time=?, " +
                "last_mark_date_and_time=?, " +
                "next_mark_date_and_time=?, " +
                "frequency=? " +
                "WHERE id=?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habit.getUserId());
            preparedStatement.setString(2, habit.getTitle());
            preparedStatement.setString(3, habit.getDescription());
            preparedStatement.setBoolean(4, habit.isCompleted());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(habit.getCreationDateAndTime()));
            preparedStatement.setTimestamp(6, habit.getLastMarkDateAndTime() != null ? Timestamp.valueOf(habit.getLastMarkDateAndTime()) : null);
            preparedStatement.setTimestamp(7, habit.getNextMarkDateAndTime() != null ? Timestamp.valueOf(habit.getNextMarkDateAndTime()) : null);
            preparedStatement.setObject(8, habit.getFrequency(), java.sql.Types.OTHER);
            preparedStatement.setLong(9, habit.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для удаления привычки из базы данных
     * @param habit сущность привычки, которую необходимо удалить
     */
    @Override
    public void delete(Habit habit) {
        String sqlQuery = "DELETE FROM entity.habit WHERE id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habit.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для удаления привычки из базы данных
     * @param habitId идентификатор привычки, которую необходимо удалить
     */
    @Override
    public void delete(long habitId) {
        String sqlQuery = "DELETE FROM entity.habit WHERE id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, habitId);

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}