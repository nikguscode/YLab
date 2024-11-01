package common.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteDto {
    @JsonProperty("userId")
    private long requiredUserId;

    @JsonIgnore
    private long sessionUserId;
}
