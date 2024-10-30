package common.dto.response.administrator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserListDto {
    private long userId;
    private String email;
    private String username;
    private String password;
    private boolean isBlocked;
}