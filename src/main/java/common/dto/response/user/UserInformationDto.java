package common.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized @Builder
public class UserInformationDto {
    private long id;
    private String email;
    private String username;
    private String password;
    private String role;
    private int amountOfHabits;
    private String registrationDate;
    private String authorizationDate;
}