package infrastructure.dto;

import lombok.*;

/**
 * Хранит пользовательский ввод при регистрации учётной записи
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class RegistrationDto {
    private String email;
    private String username;
    private String password;
}