package core.enumiration;

import core.exceptions.InvalidFrequencyConversionException;

/**
 * Класс, содержащий частоты для привычки
 */
public enum Frequency {
    UNDEFINED("Неопределено"),
    EVERY_DAY("Ежедневно"),
    EVERY_WEEK("Еженедельно"),
    EVERY_MONTH("Ежемесячно"),
    EVERY_THREE_MONTH("Раз в три месяца"),
    EVERY_SIX_MONTH("Раз в шесть месяцев"),
    EVERY_YEAR("Ежегодно");

    private final String frequency;

    Frequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * Используется для вывода пользователю привычки в виде строки
     * @return строкове представление привычки
     */
    public String getValue() {
        return this.frequency;
    }

    /**
     * Метод, который используется для преобразования пользовательского ввода в {@link Frequency}
     * @param userInput пользовательский ввод, содержащий частоту привычки
     * @return {@link Frequency}
     * @throws InvalidFrequencyConversionException возникает в случае неудачного преобразования
     */
    public static Frequency convertFromString(String userInput) throws InvalidFrequencyConversionException{
        return switch (userInput) {
            case "1", "1.", "Ежедневно", "1. Ежедневно" -> EVERY_DAY;
            case "2", "2.", "Еженедельно", "2. Еженедельно" -> EVERY_WEEK;
            case "3", "3.", "Ежемесячно", "3. Ежемесячно" -> EVERY_MONTH;
            case "4", "4.", "Раз в три месяца", "4. Раз в три месяца" -> EVERY_THREE_MONTH;
            case "5", "5.", "Раз в полгода", "5. Раз в полгода" -> EVERY_SIX_MONTH;
            case "6", "6.", "Ежегодно", "6. Ежегодно" -> EVERY_YEAR;
            default -> throw new InvalidFrequencyConversionException();
        };
    }

    /**
     * Метод, который используется для преобразования {@link Frequency} в эквивалент в виде целого числа
     * @param frequency частота, которую необходимо преобразовать
     * @return количество дней, эквивалентных частоте {@link Frequency}
     * @throws InvalidFrequencyConversionException возникает в случае неудачного преобразования
     */
    public static int convertToInteger(Frequency frequency) throws InvalidFrequencyConversionException {
        return switch (frequency) {
            case EVERY_DAY -> 1;
            case EVERY_WEEK -> 7;
            case EVERY_MONTH -> 30;
            case EVERY_THREE_MONTH -> 90;
            case EVERY_SIX_MONTH -> 180;
            case EVERY_YEAR -> 365;
            default -> throw new InvalidFrequencyConversionException();
        };
    }
}