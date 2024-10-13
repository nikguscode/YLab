package infrastructure;

import core.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, используемый в качестве локальной базы данных
 */
public class UserDatabase {
    public static Map<String, User> database = new HashMap<>();
}
