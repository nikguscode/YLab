package infrastructure.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class RegistrationDto {
    private String email;
    private String username;
    private String password;

    public RegistrationDto(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}