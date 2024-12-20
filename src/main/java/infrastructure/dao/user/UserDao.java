package infrastructure.dao.user;

import core.entity.User;

import java.util.Map;

/**
 * Интерфейс, для CRUD запросов к базе данных, связанных с сущностью {@link User}
 */
public interface UserDao {
    /**
     * Метод для добавления пользователя в базу данных
     * @param user сущность пользователя, которого нужно добавить в базу данных
     */
    void add(User user);

    /**
     * Метод для получения всех сущностей пользователя из базы данных
     * @return карту пользователей, где ключом является электронная почта пользователя
     */
    Map<String, User> getAll();

    /**
     * Метод для получения сущности пользователя из базы данных
     * @param email почта при помощи которой будет выполнен поиск пользователя
     * @return сущность пользователя
     */
    User get(String email);

    /**
     * Метод для изменения пользователя в базе данных
     * @param user отредактированная сущность пользователя, которую нужно обновить в базе данных
     */
    void edit(User user);

    /**
     * Метод для удаления пользователя из базы данных
     * @param user сущность пользователя, которую необходимо удалить
     */
    void delete(User user);
}