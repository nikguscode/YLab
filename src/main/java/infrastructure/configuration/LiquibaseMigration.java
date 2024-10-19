package infrastructure.configuration;

import core.ConfigLoader;
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

public class LiquibaseMigration {
    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    public LiquibaseMigration() {
        ConfigLoader configLoader = ConfigLoader.getInstance();
        this.URL = configLoader.getProperties("datasource.url");
        this.USERNAME = configLoader.getProperties("datasource.username");
        this.PASSWORD = configLoader.getProperties("datasource.password");
    }

    public void migrateDatabase(){
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            createSchemaIfNotExists(connection);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName("service");
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");
        } catch (SQLException | LiquibaseException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }

    private void createSchemaIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE SCHEMA IF NOT EXISTS service";
            statement.executeUpdate(sql);
            System.out.println("Schema 'service' created or already exists.");
        }
    }
}
