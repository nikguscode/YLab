package common.dto.request.habit;

import common.enumiration.Frequency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitEditDto {
    private Long habitId;
    private String title;
    private String description;
    private Boolean isCompleted;
    private Frequency frequency;

    private Long sessionId;
}