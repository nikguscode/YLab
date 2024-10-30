package common.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized @Builder
public class UserEditDto {
    @JsonProperty("userId")
    private Long requiredUserId;

    @JsonIgnore
    private Long sessionUserId;

    private String email;
    private String username;
    private String password;
}