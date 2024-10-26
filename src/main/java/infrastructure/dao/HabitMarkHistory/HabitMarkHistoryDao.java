package infrastructure.dao.HabitMarkHistory;

import core.entity.Habit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс, для CRUD запросов к базе данных, связанных с историей отметок привычки
 */
public interface HabitMarkHistoryDao {
    /**
     * Добавляет отметку через идентификатор привычки
     * @param habitId идентификатор привычки
     */
    void add(long habitId, LocalDateTime dateTime);

    /**
     * Добавляет отметку через сущность привычки
     * @param habit привычка, для которой ставится отметка
     */
    void add(Habit habit, LocalDateTime dateTime);

    /**
     * Получает все даты отметок для привычки
     * @param habitId идентификатор привычки для которой необходимо получить даты
     * @return список, содержащий даты отметок
     */
    List<LocalDateTime> getAll(long habitId);

    /**
     * Получает все даты отметок для привычки
     * @param habit привычка для которой необходимо получить даты
     * @return список, содержащий даты отметок
     */
    List<LocalDateTime> getAll(Habit habit);

    /**
     * Очищает историю отметок для указанной привычки
     * @param habit привычка, для которой выполняется очистка
     */
    void clear(Habit habit);
}