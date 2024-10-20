package core.entity;

import core.enumiration.Frequency;
import core.exceptions.InvalidHabitInformationException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Habit {
    private long id;
    private long userId;
    private String title;
    private String description;
    private boolean isCompleted;
    private LocalDateTime creationDateAndTime;
    private LocalDateTime lastMarkDateAndTime;
    private LocalDateTime nextMarkDateAndTime;
    private List<LocalDateTime> history;
    private Frequency frequency;
    private int streak;

    public void setTitle(String title) throws InvalidHabitInformationException {
        validateTitle(title);
        this.title = title;
    }

    public void setDescription(String description) throws InvalidHabitInformationException {
        validateDescription(description);
        this.description = description;
    }

    private static void validateTitle(String title) throws InvalidHabitInformationException {
        if (title.isEmpty()) {
            System.out.println("Название привычки не может быть пустым!");
            throw new InvalidHabitInformationException();
        }
    }

    private static void validateDescription(String description) throws InvalidHabitInformationException {
        if (description.isEmpty()) {
            System.out.println("Описание привычки не может быть пустым!");
            throw new InvalidHabitInformationException();
        }
    }

    public static class HabitBuilder {
        private long userId;
        private String title;
        private String description;
        private LocalDateTime creationDateAndTime;

        public HabitBuilder userId(long userId) throws InvalidHabitInformationException {
            if (userId == 0) {
                throw new InvalidHabitInformationException("userId - обязательное поле при создании привычки");
            }

            this.userId = userId;
            return this;
        }

        public HabitBuilder title(String title) throws InvalidHabitInformationException {
            validateTitle(title);
            this.title = title;
            return this;
        }

        public HabitBuilder description(String description) throws InvalidHabitInformationException {
            validateDescription(description);
            this.description = description;
            return this;
        }

        public HabitBuilder creationDateAndTime(LocalDateTime creationDateAndTime) throws InvalidHabitInformationException {
            if (creationDateAndTime == null) {
                System.out.println("Ошибка даты и времени создания привычки, попробуйте ещё раз!");
                throw new InvalidHabitInformationException();
            }

            this.creationDateAndTime = creationDateAndTime;
            return this;
        }

        public Habit build() {
            return new Habit(id, userId, title, description, isCompleted, creationDateAndTime, lastMarkDateAndTime,
                    nextMarkDateAndTime, history, frequency, streak);
        }
    }
}