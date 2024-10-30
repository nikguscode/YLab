package common.dto.request.authentication;

import lombok.*;

/**
 * Хранит пользовательский ввод при входе в учётную запись
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    private String email;
    private String password;
}