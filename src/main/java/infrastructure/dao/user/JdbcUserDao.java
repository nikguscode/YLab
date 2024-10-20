package infrastructure.dao.user;

import core.entity.User;
import core.enumiration.Role;
import core.exceptions.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JdbcUserDao implements UserDao {
    private final DatabaseUtils databaseUtils;

    @Override
    public void add(User user) {
        String sqlQuery = "INSERT INTO entity.user" +
                "(email, username, password, role, is_authorized, registration_date, authorization_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setObject(4, user.getRole(), java.sql.Types.OTHER);
            preparedStatement.setBoolean(5, user.isAuthorized());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(user.getRegistrationDate()));
            preparedStatement.setTimestamp(7, user.getAuthorizationDate() != null ? Timestamp.valueOf(user.getAuthorizationDate()) : null);

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
                        .registrationDate(rs.getTimestamp("registration_date").toLocalDateTime())
                        .authorizationDate(rs.getTimestamp("authorization_date").toLocalDateTime())
                        .build();
            }
        } catch (SQLException | ClassNotFoundException | InvalidUserInformationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void edit(User user) {
        String sqlQuery = "UPDATE entity.user " +
                "SET email=?, " +
                "username=?, " +
                "password=?, " +
                "role=?, " +
                "is_authorized=?, " +
                "authorization_date=? " +
                "WHERE id=?";

        try (Connection connection = databaseUtils.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setObject(4, user.getRole(), java.sql.Types.OTHER);
            preparedStatement.setBoolean(5, user.isAuthorized());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(user.getAuthorizationDate()));
            preparedStatement.setLong(7, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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