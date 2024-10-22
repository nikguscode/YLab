package infrastructure.dto;

import lombok.*;

/**
 * Хранит пользовательский ввод при входе в учётную запись
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