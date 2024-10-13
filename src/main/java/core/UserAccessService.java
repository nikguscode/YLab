package core;

import core.entity.User;
import core.enumiration.Role;
import infrastructure.dao.user.UserDao;

/**
 * Класс, предназначенный для проверки доступа пользователя к приложению
 */
public class UserAccessService {
    /**
     * Метод, определяющий, есть ли у пользователя доступ к приложению. В случае, если доступа нет, сообщает об этом
     * контроллеру
     * @param userDao интерфейс для CRUD операций к базе данных пользователя
     * @param email электронная почта для определения пользователя
     * @return <b>true</b>: в случае, если доступ есть, иначе <b>false</b>
     */
    public static boolean hasAccess(UserDao userDao, String email) {
        User user = userDao.get(email);

        if (userDao.get(email) == null) {
            return false;
        }

        if (!user.isAuthorized()) {
            return false;
        }

        if (user.getRole() == Role.BLOCKED) {
            return false;
        }

        return true;
    }
}