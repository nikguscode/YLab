import adapters.controller.AuthenticationController;
import adapters.controller.MainController;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.configuration.LiquibaseMigration;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.user.JdbcUserDao;
import usecase.authentication.login.LocalLogin;
import usecase.authentication.registration.LocalRegistration;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class YLabMain {
    public static void main(String[] args) {
        new LiquibaseMigration().migrateDatabase();

        Scanner scanner = new Scanner(System.in);
        AuthenticationController authenticationController = new AuthenticationController(
                new MainController(new JdbcUserDao(), new JdbcHabitDao()),
                new LocalLogin(),
                new LocalRegistration(),
                new JdbcUserDao(),
                new JdbcHabitDao()
        );

        while (true) {
            try {
                authenticationController.handle(scanner);
            } catch (NoSuchElementException | IllegalStateException | InvalidUserInformationException
                     | InvalidFrequencyConversionException ignored) {
            }
        }
    }
}