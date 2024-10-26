package core.enumiration;

import core.exceptions.InvalidFrequencyConversionException;

/**
 * Класс, содержащий частоты для привычки
 */
public enum Frequency {
    UNDEFINED("Неопределено", 0),
    EVERY_DAY("Ежедневно", 1),
    EVERY_WEEK("Еженедельно", 7),
    EVERY_MONTH("Ежемесячно", 30),
    EVERY_THREE_MONTH("Раз в три месяца", 90),
    EVERY_SIX_MONTH("Раз в шесть месяцев", 180),
    EVERY_YEAR("Ежегодно", 365);

    private final String stringFrequency;
    private final int integerFrequency;

    Frequency(String stringFrequency, int integerFrequency) {
        this.stringFrequency = stringFrequency;
        this.integerFrequency = integerFrequency;
    }

    /**
     * Используется для вывода пользователю привычки в виде строки
     * @return строкове представление привычки
     */
    public String getStringValue() {
        return this.stringFrequency;
    }

    /**
     * Используется для вывода пользователю привычки в виде целого числа
     * @return числое представление привычки
     */
    public int getIntegerValue() {
        return this.integerFrequency;
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
}