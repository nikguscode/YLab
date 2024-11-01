package common.enumiration;

/**
 * Класс, содержащий частоты для привычки
 */
public enum Frequency {
    UNDEFINED("UNDEFINED", 0),
    DAILY("DAILY", 1),
    WEEKLY("WEEKLY", 7),
    MONTHLY("MONTHLY", 30),
    HALF_YEARLY("HALF_YEARLY", 180),
    YEARLY("YEARLY", 365);

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
}