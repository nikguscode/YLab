package common.dto.response.habit.listdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitListDto {
    private Long habitId;
    private String title;
    private String description;
    private String creationDateAndTime;
    private String lastMarkDateAndTime;
    private String nextMarkDateAndTime;
    private String frequency;
    private int streak;

    @JsonProperty("isCompleted")
    private boolean isCompleted;
}