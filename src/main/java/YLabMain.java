import adapters.controller.AuthenticationController;
import adapters.controller.MainController;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.LocalUserDao;
import usecase.authentication.login.LocalLogin;
import usecase.authentication.registration.LocalRegistration;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class YLabMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthenticationController authenticationController = new AuthenticationController(
                new MainController(new LocalUserDao()),
                new LocalLogin(),
                new LocalRegistration()
        );

        while (true) {
            try {
                authenticationController.handle(scanner);
            } catch (NoSuchElementException | IllegalStateException | InterruptedException |
                     InvalidUserInformationException | InvalidFrequencyConversionException |
                     InvalidHabitInformationException e) {
            }
        }
    }
}