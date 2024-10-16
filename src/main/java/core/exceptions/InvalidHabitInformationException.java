package core.exceptions;

/**
 * Исключение, которое выбрасывается в том случае, если {@link core.entity.Habit Habit} инициализирует некорректные данные
 * или происходит изменение полей на некорректные значения
 */
public class InvalidHabitInformationException extends Throwable {
    public InvalidHabitInformationException() {
        super();
    }

    public InvalidHabitInformationException(String errorMessage) {
        super(errorMessage);
    }
}