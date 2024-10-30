package common.dto.response.habit;

import common.enumiration.Frequency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitInformationDto {
    long id;
    long userId;
    String title;
    String description;
    boolean isCompleted;
    String creationDateAndTime;
    String lastMarkDateAndTime;
    String nextMarkDateAndTime;
    String frequency;
}