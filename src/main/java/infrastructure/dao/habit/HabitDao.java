package infrastructure.dao.habit;

import core.entity.Habit;
import core.entity.User;

import java.util.Map;

/**
 * Класс, для CRUD запросов к базе данных, связанных с сущностью {@link Habit}
 */
public interface HabitDao {
    /**
     * Метод для добавления привычки в базу данных
     * @param habit сущность привычки, которую нужно добавить в базу данных
     */
    long add(Habit habit);

    /**
     * Метод для получения всех сущностей привычки из базы данных
     * @return список привычек, где ключом является идентификатор привычки
     */
    Map<Long, Habit> getAll(User user);

    /**
     * Метод для получения сущности привычки из базы данных
     * @param id при помощи которого будет выполнен поиск привычки
     * @return сущность привычки
     */
    Habit get(long id);

    /**
     * Метод для изменения привычки в базе данных
     * @param habit отредактированная сущность привычки, которую нужно обновить в базе данных
     */
    void edit(Habit habit);

    /**
     * Метод для удаления привычки из базы данных
     * @param habit сущность привычки, которую необходимо удалить
     */
    void delete(Habit habit);
}
