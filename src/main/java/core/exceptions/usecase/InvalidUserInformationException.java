package core.exceptions.usecase;

/**
 * Исключение, которое выбрасывается в том случае, если {@link core.entity.User User} инициализирует некорректные данные
 * или происходит изменение полей на некорректные значения
 */
public class InvalidUserInformationException extends Exception {
    public InvalidUserInformationException() {
        super();
    }

    public InvalidUserInformationException(String errorMessage) {
        super(errorMessage);
    }
}