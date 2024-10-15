package infrastructure.dto;

import lombok.*;

/**
 * Класс для переноса данных в сервисы. Пока приложение не содержит базу данных, переносит данные пользовательского ввода
 * в нужный сервис
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class LoginDto {
    private final String email;
    private final String password;
}