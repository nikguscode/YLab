import adapters.controller.AuthenticationController;
import adapters.controller.MainController;
import core.ConfigLoaderService;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dao.HabitMarkHistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.user.JdbcUserDao;
import usecase.authentication.login.JdbcLogin;
import usecase.authentication.registration.JdbcRegistration;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class YLabMain {
    private static final ConfigLoaderService configLoaderService = ConfigLoaderService.getInstance();
    private static final String DATABASE_DRIVER = configLoaderService.getProperties("datasource.driver");
    private static final String DATABASE_URL = configLoaderService.getProperties("datasource.url");
    private static final String DATABASE_USERNAME = configLoaderService.getProperties("datasource.username");
    private static final String DATABASE_PASSWORD = configLoaderService.getProperties("datasource.password");

    public static void main(String[] args) {
        new LiquibaseMigration().migrateDatabase();
        Scanner scanner = new Scanner(System.in);
        AuthenticationController authenticationController = getAuthenticationController();

        while (true) {
            try {
                authenticationController.handle(scanner);
            } catch (NoSuchElementException | IllegalStateException | InvalidUserInformationException
                     | InvalidFrequencyConversionException e) {
                e.printStackTrace();
            }
        }
    }

    private static AuthenticationController getAuthenticationController() {
        DatabaseUtils databaseUtils = new DatabaseUtils(DATABASE_DRIVER, DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

        return new AuthenticationController(
                new MainController(
                        new JdbcUserDao(databaseUtils),
                        new JdbcHabitDao(databaseUtils, new JdbcHabitMarkHistoryDao(databaseUtils)),
                        new JdbcHabitMarkHistoryDao(databaseUtils)
                ),
                new JdbcLogin(new JdbcUserDao(databaseUtils)),
                new JdbcRegistration(new JdbcUserDao(databaseUtils)),
                new JdbcUserDao(databaseUtils),
                new JdbcHabitDao(databaseUtils, new JdbcHabitMarkHistoryDao(databaseUtils))
        );
    }
}