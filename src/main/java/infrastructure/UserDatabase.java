package infrastructure;

import core.entity.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, используемый в качестве локальной базы данных
 */
public class UserDatabase {
    private static final Map<String, User> database = new HashMap<>();

    /**
     * Метод для добавления пользователя в локальную базу данных
     * @param email почта по которой можно идентифицировать пользователя
     * @param user экземпляр сущности пользователя
     */
    public static void add(String email, User user) {
        database.put(email, user);
    }

    /**
     * Метод для извлечения пользователя из базы данных
     * @param email почта по которой можно идентифицировать пользователя
     * @return экземпляр сущности пользователя
     */
    public static User get(String email) {
        return database.get(email);
    }

    /**
     * Метод для извлечения всех пользователей из локальной базы данных
     * @return коллекцию, содержащую пользователей
     */
    public static Collection<User> values() {
        return database.values();
    }

    /**
     * Метод для удаления пользователя из базы данных
     * @param email почта по которой можно идентифицировать пользователя
     */
    public static void remove(String email) {
        database.remove(email);
    }
}