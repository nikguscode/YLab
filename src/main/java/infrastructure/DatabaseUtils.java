package infrastructure;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtils {
    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);

        if ((username == null) || (password == null) || (username.trim().isEmpty()) || (password.trim().isEmpty())) {
            return DriverManager.getConnection(url);
        } else {
            return DriverManager.getConnection(url, username, password);
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> map(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            if (resultSet != null) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int amountOfColumns = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int currentColumn = 1; currentColumn <= amountOfColumns; currentColumn++) {
                        String tableName = metaData.getColumnName(currentColumn);
                        Object value = resultSet.getObject(currentColumn);
                        row.put(tableName, value);
                    }

                    results.add(row);
                }
            }
        } finally {
            close(resultSet);
        }

        return results;
    }
}