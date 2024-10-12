package infrastructure.dao.habit;

import core.entity.Habit;

import java.util.UUID;

public class LocalHabitDao implements HabitDao {
    @Override
    public void add(Habit habit) {
       // for jdbc implementation
    }

    @Override
    public Habit get(UUID id) {
        // for jdbc implementation
        return null;
    }

    @Override
    public void delete(Habit habit) {
        // for jdbc implementation
    }

    @Override
    public void edit(Habit habit) {
        // for jdbc implementation
    }
}