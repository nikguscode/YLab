package core.exceptions.usecase;

import common.enumiration.Frequency;

/**
 * Исключение, которое выбрасывается в том случае, если {@link Frequency#convertToInteger(Frequency)
 * convertToInteger()} или {@link Frequency#convertFromString(String) convertFromString()} заканчиваются неудачей
 */
public class InvalidFrequencyConversionException extends Exception {
    public InvalidFrequencyConversionException() {
        super();
    }
}
