package adapters.in;

import java.util.Scanner;

/**
 * Интефрейс, обрабатывающий пользовательский ввод
 * @param <T>
 */
public interface ConsoleInput<T> {
    /**
     * Метод для пользовательского ввода
     * @param scanner экземпляр сканера
     * @return возвращает DTO класс, содержащий данные, заполненные пользовательским вводом
     */
    T input(Scanner scanner);
}
