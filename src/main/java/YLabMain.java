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
    /**
     * Экземпляр {@link ConfigLoaderService} для получения данных из файла конфигурации
     */
    private static final ConfigLoaderService configLoaderService = ConfigLoaderService.getInstance();

    /**
     * Драйвер для базы данных
     */
    private static final String DATABASE_DRIVER = configLoaderService.getProperties("datasource.driver");

    /**
     * Ссылка для базы данных
     */
    private static final String DATABASE_URL = configLoaderService.getProperties("datasource.url");

    /**
     * Имя пользователя для базы данных
     */
    private static final String DATABASE_USERNAME = configLoaderService.getProperties("datasource.username");

    /**
     * Пароль для базы данных
     */
    private static final String DATABASE_PASSWORD = configLoaderService.getProperties("datasource.password");

    /**
     * Точка входа в программу
     * @param args обязательный параметр
     */
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

    /**
     * Создаёт необходимые экземпляры
     * @return экземпляр контроллера для аутентификации, который является точкой входа в приложение
     */
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