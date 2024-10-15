package infrastructure.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class HabitDto {
    private String title;
    private String description;
    private String dateAndTime;
    private String frequency;
}