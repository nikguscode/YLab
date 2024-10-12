package core.enumiration;

public enum Role {
    UNDEFINED("неопредёленный"),
    BLOCKED("заблокированный"),
    USER("пользователь"),
    ADMINISTRATOR("администратор");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getValue() {
        return this.role;
    }
}