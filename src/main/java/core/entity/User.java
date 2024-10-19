package core.entity;

import core.enumiration.Role;
import core.exceptions.InvalidUserInformationException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class User {
    private long id;
    private String email;
    private String username;
    private String password;
    private Role role;
    private boolean isAuthorized;
    private Map<Long, Habit> habits;
    private LocalDateTime registrationDate;
    private LocalDateTime authorizationDate;

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

    public static class UserBuilder {
        private String email;
        private String username;
        private String password;

        public UserBuilder email(String email) throws InvalidUserInformationException {
            validateEmail(email);
            this.email = email;
            return this;
        }

        public UserBuilder username(String username) throws InvalidUserInformationException {
            validateUsername(username);
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) throws InvalidUserInformationException {
            validatePassword(password);
            this.password = password;
            return this;
        }

        public User build() {
            if (habits == null) {
                this.habits = new HashMap<>();
            }

            return new User(id, email, username, password, role, isAuthorized, habits, registrationDate, authorizationDate);
        }
    }
}