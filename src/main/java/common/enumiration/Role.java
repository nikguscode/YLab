package common.enumiration;

/**
 * Класс, содержащий роли для пользователя
 */
public enum Role {
    UNDEFINED("неопредёленный"),
    BLOCKED("заблокированный"),
    USER("пользователь"),
    ADMINISTRATOR("администратор");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    /**
     * Используется для вывода роли в виде строки
     * @return строкове представление роли
     */
    public String getValue() {
        return this.role;
    }
}