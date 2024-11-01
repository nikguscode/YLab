package common.dto.request.habit;

import common.enumiration.Frequency;
import lombok.*;

/**
 * Хранит пользовательский ввод при создании привычки
 */
@Getter
@Setter
public class HabitPostDto {
    private String title;
    private String description;
    private Frequency frequency;
    private String markDateAndTime;
}