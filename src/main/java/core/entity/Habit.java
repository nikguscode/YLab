package core.entity;

import core.enumiration.Frequency;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Habit {
    public static long listId = 1;
    private long id;
    private UUID userId;
    private String title;
    private String description;
    private boolean isCompleted;
    private LocalDateTime creationDateAndTime;
    private LocalDateTime lastMarkDateAndTime;
    private LocalDateTime shiftedDateAndTime;
    private List<LocalDateTime> history;
    private Frequency frequency;

    private Habit(Builder builder) {
        this.id = listId++;
        this.userId = builder.userId;
        this.title = builder.title;
        this.description = builder.description;
        this.isCompleted = false;
        this.creationDateAndTime = builder.creationDateAndTime;
        this.lastMarkDateAndTime = builder.lastMarkDateAndTime;
        this.history = builder.history;
        this.frequency = builder.frequency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID userId;
        private String title;
        private String description;
        private LocalDateTime creationDateAndTime;
        private LocalDateTime lastMarkDateAndTime;
        private LocalDateTime shiftedDateAndTime;
        private List<LocalDateTime> history;
        private Frequency frequency;

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder creationDateAndTime(LocalDateTime creationDateAndTime) {
            this.creationDateAndTime = creationDateAndTime;
            return this;
        }

        public Builder lastMarkDateAndTime(LocalDateTime lastMarkDateAndTime) {
            this.lastMarkDateAndTime = lastMarkDateAndTime;
            return this;
        }

        public Builder shiftedDateAndTime(LocalDateTime shiftedDateAndTime) {
            this.shiftedDateAndTime = shiftedDateAndTime;
            return this;
        }

        public Builder history(List<LocalDateTime> history) {
            this.history = history;
            return this;
        }

        public Builder frequency(Frequency frequency) {
            this.frequency = frequency;
            return this;
        }

        public Habit build() {
            return new Habit(this);
        }
    }
}