package infrastructure.dao.habit;

import core.entity.Habit;

import java.util.Map;

/**
 * Класс, для CRUD запросов к базе данных, связанных с сущностью {@link Habit}
 */
public interface HabitDao {
    /**
     * Метод для добавления привычки в базу данных
     * @param habit сущность привычки, которую нужно добавить в базу данных
     */
    void add(Habit habit);

    /**
     * Метод для получения всех сущностей привычки из базы данных
     * @return список привычек, где ключом является идентификатор привычки
     */
    Map<Long, Habit> get();

    /**
     * Метод для получения сущности привычки из базы данных
     * @param id при помощи которого будет выполнен поиск привычки
     * @return сущность привычки
     */
    Habit get(long id);

    void edit(Habit habit);

    void delete(Habit habit);
}
