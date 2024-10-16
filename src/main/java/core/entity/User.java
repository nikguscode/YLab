package core.entity;

import core.enumiration.Role;
import core.exceptions.InvalidUserInformationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private UUID id;
    private String email;
    private String username;
    private String password;
    private Role role;
    private boolean isAuthorized;
    private Map<Long, Habit> habits;
    private LocalDateTime registrationDate;
    private LocalDateTime authorizationDate;

    private User(Builder builder) {
        this.id = UUID.randomUUID();
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
        this.isAuthorized = builder.isAuthorized;
        this.habits = builder.habits;
        this.registrationDate = builder.registrationDate;
        this.authorizationDate = builder.authorizationDate;
    }

    public void setEmail(String email) throws InvalidUserInformationException {
        validateEmail(email);
        this.email = email;
    }

    public void setUsername(String username) throws InvalidUserInformationException {
        validateUsername(username);
        this.username = username;
    }

    public void setPassword(String password) throws InvalidUserInformationException {
        validatePassword(password);
        this.password = password;
    }

    private static void validateEmail(String email) throws InvalidUserInformationException {
        if (!email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            System.out.println("Некорректный формат электронной почты!");
            throw new InvalidUserInformationException();
        }
    }

    private static void validateUsername(String username) throws InvalidUserInformationException {
        if (username.isEmpty()) {
            System.out.println("Имя пользователя обязательно к заполнению!");
            throw new InvalidUserInformationException();
        }
    }

    private static void validatePassword(String password) throws InvalidUserInformationException {
        if (password.isEmpty()) {
            System.out.println("Пароль обязателен к заполнению!");
            throw new InvalidUserInformationException();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String username;
        private String password;
        private Role role;
        private boolean isAuthorized;
        private Map<Long, Habit> habits;
        private LocalDateTime registrationDate;
        private LocalDateTime authorizationDate;

        public Builder email(String email) throws InvalidUserInformationException {
            validateEmail(email);
            this.email = email;
            return this;
        }

        public Builder username(String username) throws InvalidUserInformationException {
            validateUsername(username);
            this.username = username;
            return this;
        }

        public Builder password(String password) throws InvalidUserInformationException {
            validatePassword(password);
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder isAuthorized(boolean isAuthorized) {
            this.isAuthorized = isAuthorized;
            return this;
        }

        public Builder habits(Map<Long, Habit> habits) {
            this.habits = habits;
            return this;
        }

        public Builder registrationDate(LocalDateTime registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder authorizationDate(LocalDateTime authorizationDate) {
            this.authorizationDate = authorizationDate;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}