package common.dto.request.habit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitDeleteDto {
    private Long habitId;

    @JsonIgnore
    private Long sessionUserId;
}