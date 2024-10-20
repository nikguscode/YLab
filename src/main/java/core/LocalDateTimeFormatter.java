package core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Класс, предназначенный для конвертации LocalDateTime в String и последующем форматировании
 */
public class LocalDateTimeFormatter {
    /**
     * Конвертирует и форматирует дату
     * @param dateTime дата и время, которые необходимо конвертировать и форматировать
     * @return дату и время в виде форматированной строки
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
    }
}
