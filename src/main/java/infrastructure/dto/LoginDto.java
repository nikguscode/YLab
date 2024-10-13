package infrastructure.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс для переноса данных в сервисы. Пока приложение не содержит базу данных, переносит данные пользовательского ввода
 * в нужный сервис
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class LoginDto {
    private final String email;
    private final String password;

    private LoginDto(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String password;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public LoginDto build() {
            return new LoginDto(this);
        }
    }
}