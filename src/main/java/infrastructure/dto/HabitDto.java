package infrastructure.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HabitDto {
    private String title;
    private String description;
    private String dateAndTime;
    private String frequency;

    private HabitDto(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.dateAndTime = builder.dateAndTime;
        this.frequency = builder.frequency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String description;
        private String dateAndTime;
        private String frequency;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder dateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
            return this;
        }

        public Builder frequency(String frequency) {
            this.frequency = frequency;
            return this;
        }

        public HabitDto build() {
            return new HabitDto(this);
        }
    }
}