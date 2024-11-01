package common.dto.response.habit.informationdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitInformationDto {
    long id;
    String title;
    String description;
    boolean isCompleted;
    String creationDateAndTime;
    String lastMarkDateAndTime;
    String nextMarkDateAndTime;
    String frequency;
    int streak;
}