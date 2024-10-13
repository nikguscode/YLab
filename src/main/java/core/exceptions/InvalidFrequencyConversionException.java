package core.exceptions;

import core.enumiration.Frequency;

/**
 * Исключение, которое выбрасывается в том случае, если {@link core.enumiration.Frequency#convertToInteger(Frequency)
 * convertToInteger()} или {@link Frequency#convertFromString(String) convertFromString()} заканчиваются неудачей
 */
public class InvalidFrequencyConversionException extends Throwable {
    public InvalidFrequencyConversionException() {
        super();
    }
}
