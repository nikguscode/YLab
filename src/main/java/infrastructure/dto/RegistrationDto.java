package infrastructure.dto;

import lombok.*;

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