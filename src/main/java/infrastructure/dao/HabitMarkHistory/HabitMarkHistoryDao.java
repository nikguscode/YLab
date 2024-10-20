package infrastructure.dao.HabitMarkHistory;

import core.entity.Habit;

import java.time.LocalDateTime;
import java.util.List;

public interface HabitMarkHistoryDao {
    /**
     * Добавляет отметку через идентификатор привычки
     * @param habitId идентификатор привычки
     */
    void add(long habitId);

    /**
     * Добавляет отметку через сущность привычки
     * @param habit привычка, для которой ставится отметка
     */
    void add(Habit habit);

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