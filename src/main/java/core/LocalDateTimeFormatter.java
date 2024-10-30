package core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Класс, предназначенный для конвертации LocalDateTime в String и последующем форматировании
 */
public class LocalDateTimeFormatter {
    /**
     * Конвертирует и формутирует текущую дату и время с секундами
     * @return дату и время в виде форматированной строки
     */
    public static String formatWithSeconds() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
    }

    /**
     * Конвертирует и форматирует дату и время с секундами
     * @param dateTime дата и время, которые необходимо конвертировать и форматировать
     * @return дату и время в виде форматированной строки
     */
    public static String formatWithSeconds(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
    }

    /**
     * Конвертирует и форматирует текущую дату и время
     * @return дату и время в виде форматированной строки
     */
    public static String formatWithMinutes() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
    }

    /**
     * Конвертирует и форматирует дату и время
     * @param dateTime дата и время, которые необходимо конвертировать и форматировать
     * @return дату и время в виде форматированной строки
     */
    public static String formatWithMinutes(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
    }
}