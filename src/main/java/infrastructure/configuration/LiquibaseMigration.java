package infrastructure.configuration;

import core.ConfigLoaderService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для запуска миграции базы данных
 */
public class LiquibaseMigration {
    /**
     * JDBC драйвер для работы с базой данных
     */
    private final String JDBC_DRIVER;

    /**
     * Ссылка на базу данных
     */
    private final String URL;

    /**
     * Имя пользователя для взаимодействия с базой данных
     */
    private final String USERNAME;

    /**
     * Пароль для взаимодействия с базой данных
     */
    private final String PASSWORD;

    /**
     * Конструктор для инициализации параметров базы данных из конфигурационного файла
     */
    public LiquibaseMigration() {
        ConfigLoaderService configLoader = ConfigLoaderService.getInstance();
        this.JDBC_DRIVER = configLoader.getProperties("datasource.driver");
        this.URL = configLoader.getProperties("datasource.url");
        this.USERNAME = configLoader.getProperties("datasource.username");
        this.PASSWORD = configLoader.getProperties("datasource.password");
    }

    /**
     * Конструктор для инициализации параметров базы данных при тестировании
     * @param url ссылка на базу данных
     * @param username имя пользователя
     * @param password пароль
     */
    public LiquibaseMigration(String url, String username, String password) {
        ConfigLoaderService configLoader = ConfigLoaderService.getInstance();
        this.JDBC_DRIVER = configLoader.getProperties("datasource.driver");
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
    }

    /**
     * Выполняет миграцию
     */
    public void migrateDatabase(){
        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            createSchemaIfNotExists(connection);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName("service");
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");
        } catch (SQLException | LiquibaseException | ClassNotFoundException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }

    /**
     * Создаёт служебную схему для хранения информации Liquibase, если такой схемы ещё не существует
     * @param connection текущее соединение
     * @throws SQLException
     */
    private void createSchemaIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE SCHEMA IF NOT EXISTS service";
            statement.executeUpdate(sql);
            System.out.println("Schema 'service' created or already exists.");
        }
    }
}
