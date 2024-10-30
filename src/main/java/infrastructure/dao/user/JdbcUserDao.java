package infrastructure.dao.user;

import core.entity.User;
import common.enumiration.Role;
import core.exceptions.usecase.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC реализация взаимодействия с базой данных, содержащая только CRUD операции
 */
@RequiredArgsConstructor
public class JdbcUserDao implements UserDao {
    /**
     * Экземпляр вспомогательного класса для взаимодействия с базой данных
     */
    private final DatabaseUtils databaseUtils;

    /**
     * Метод для добавления пользователя в базу данных
     * @param user сущность пользователя, которого нужно добавить в базу данных
     */
    @Override
    public void add(User user) {
        String sqlQuery = "INSERT INTO entity.user" +
                "(email, username, password, role, is_authorized, is_blocked, registration_date, authorization_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setObject(4, user.getRole(), java.sql.Types.OTHER);
            preparedStatement.setBoolean(5, user.isAuthorized());
            preparedStatement.setBoolean(6, user.isBlocked());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(user.getRegistrationDate()));
            preparedStatement.setTimestamp(8, user.getAuthorizationDate() != null ? Timestamp.valueOf(user.getAuthorizationDate()) : null);

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения всех сущностей пользователя из базы данных
     * @return карту пользователей, где ключом является электронная почта пользователя
     */
    @Override
    public Map<String, User> getAll() {
        String sqlQuery = "SELECT * FROM entity.user";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            List<Map<String, Object>> rows = databaseUtils.map(preparedStatement.executeQuery());
            Map<String, User> mapOfUsers = new HashMap<>();

            for (Map<String, Object> currentRow : rows) {
                mapOfUsers.put(
                        (String) currentRow.get("email"),
                        User.builder()
                                .id((long) currentRow.get("id"))
                                .email((String) currentRow.get("email"))
                                .username((String) currentRow.get("username"))
                                .password((String) currentRow.get("password"))
                                .role(Role.valueOf(((String) currentRow.get("role")).toUpperCase()))
                                .isAuthorized((boolean) currentRow.get("is_authorized"))
                                .isBlocked((boolean) currentRow.get("is_blocked"))
                                .registrationDate(((Timestamp) currentRow.get("registration_date")).toLocalDateTime())
                                .authorizationDate(((Timestamp) currentRow.get("authorization_date")).toLocalDateTime())
                                .build()
                );
            }

            return mapOfUsers;
        } catch (SQLException | ClassNotFoundException | InvalidUserInformationException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Метод для получения сущности пользователя из базы данных
     * @param email почта при помощи которой будет выполнен поиск пользователя
     * @return сущность пользователя
     */
    @Override
    public User get(String email) {
        String sqlQuery = "SELECT * FROM entity.user WHERE email=?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return User.builder()
                        .id(rs.getLong("id"))
                        .email(rs.getString("email"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .role(Role.valueOf(rs.getString("role").toUpperCase()))
                        .isAuthorized(rs.getBoolean("is_authorized"))
                        .isBlocked(rs.getBoolean("is_blocked"))
                        .registrationDate(rs.getTimestamp("registration_date").toLocalDateTime())
                        .authorizationDate(rs.getTimestamp("authorization_date").toLocalDateTime())
                        .build();
            }
        } catch (SQLException | ClassNotFoundException | InvalidUserInformationException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Метод для получения сущности пользователя из базы данных
     * @param id идентификатор при помощи которого будет выполнен поиск пользователя
     * @return сущность пользователя
     */
    @Override
    public User get(long id) {
        String sqlQuery = "SELECT * FROM entity.user WHERE id=?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return User.builder()
                        .id(rs.getLong("id"))
                        .email(rs.getString("email"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .role(Role.valueOf(rs.getString("role").toUpperCase()))
                        .isAuthorized(rs.getBoolean("is_authorized"))
                        .isBlocked(rs.getBoolean("is_blocked"))
                        .registrationDate(rs.getTimestamp("registration_date").toLocalDateTime())
                        .authorizationDate(rs.getTimestamp("authorization_date").toLocalDateTime())
                        .build();
            }
        } catch (SQLException | ClassNotFoundException | InvalidUserInformationException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Метод для изменения пользователя в базе данных
     * @param user отредактированная сущность пользователя, которую нужно обновить в базе данных
     */
    @Override
    public void edit(User user) {
        String sqlQuery = "UPDATE entity.user " +
                "SET email=?, " +
                "username=?, " +
                "password=?, " +
                "role=?, " +
                "is_authorized=?, " +
                "is_blocked=?, " +
                "authorization_date=? " +
                "WHERE id=?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setObject(4, user.getRole(), java.sql.Types.OTHER);
            preparedStatement.setBoolean(5, user.isAuthorized());
            preparedStatement.setBoolean(6, user.isBlocked());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(user.getAuthorizationDate()));
            preparedStatement.setLong(8, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для удаления пользователя из базы данных
     * @param userId идентификатор пользователя, по которому он удаляется
     */
    @Override
    public void delete(long userId) {
        String sqlQuery = "DELETE FROM entity.user WHERE id = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для удаления пользователя из базы данных
     * @param user сущность пользователя, которую необходимо удалить
     */
    @Override
    public void delete(User user) {
        String sqlQuery = "DELETE FROM entity.user WHERE email = ?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}