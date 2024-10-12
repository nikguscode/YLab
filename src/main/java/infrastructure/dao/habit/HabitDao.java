package infrastructure.dao.habit;

import core.entity.Habit;

import java.util.UUID;

public interface HabitDao {
    void add(Habit habit);
    Habit get(UUID id);
    void edit(Habit habit);
    void delete(Habit habit);
}
