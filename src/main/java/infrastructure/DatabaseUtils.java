package infrastructure;

import core.ConfigLoaderService;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Вспомогательный класс для взаимодействия с базой данных
 */
@RequiredArgsConstructor
public class DatabaseUtils {
    private final String driver;
    private final String url;
    private final String username;
    private final String password;

    public DatabaseUtils() {
        ConfigLoaderService configLoader = ConfigLoaderService.getInstance();
        this.driver = configLoader.getProperties("datasource.driver");
        this.url = configLoader.getProperties("datasource.url");
        this.username = configLoader.getProperties("datasource.username");
        this.password = configLoader.getProperties("datasource.password");
    }

    /**
     * Открывает соединение с базой данных
     * @return экземпляр {@link Connection}
     * @throws ClassNotFoundException класс-драйвер базы данных не найден
     * @throws SQLException
     */
    public Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);

        if ((username == null) || (password == null) || (username.trim().isEmpty()) || (password.trim().isEmpty())) {
            return DriverManager.getConnection(url);
        } else {
            return DriverManager.getConnection(url, username, password);
        }
    }

    /**
     * Используется для закрытия соединения
     * @param connection текущее соединение
     */
    public void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Используется для закрытия ресурсов шаблона
     * @param statement текущий шаблон
     */
    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Используется для закрытия ресурсов {@link ResultSet}
     * @param resultSet текущий {@link ResultSet}
     */
    public void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Используется для отмены транзакции
     * @param connection текущее соединение
     */
    public void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Используется для получения строк из базы данных в виде списка
     * @param resultSet результат запроса к базе данных
     * @return строки из базы данных в виде списка
     * @throws SQLException
     */
    public List<Map<String, Object>> map(ResultSet resultSet) throws SQLException {
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