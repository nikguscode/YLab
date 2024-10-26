package core.exceptions;

/**
 * Исключение, которое выбрасывается в том случае, если пользователь указывает некорректный идентификатор при выводе
 * списка привычек
 */
public class InvalidHabitIdException extends Exception {
    public InvalidHabitIdException() {
        super();
    }
}
