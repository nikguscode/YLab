package infrastructure.dao.HabitMarkHistory;

import core.entity.Habit;

import java.time.LocalDateTime;
import java.util.List;

public interface HabitMarkHistoryDao {
    /**
     * Метод, который добавляет в базу данных дату и время на момент выполнения отметки
     * @param habit привычка, которую необходимо добавить
     */
    void add(Habit habit);

    /**
     * Метод для получения всех дат, связанных с привычкой
     * @param habit привычка для которой необходимо получить даты
     * @return список, содержащий даты отметок для выбранной привычки
     */
    List<LocalDateTime> getAll(Habit habit);

    /**
     * Очистить историю отметок для указанной привычки
     * @param habit привычка, для которую необходимо очистить историю отметок
     */
    void clear(Habit habit);
}