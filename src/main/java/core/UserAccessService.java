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
     * контроллеру. Проверяет, выполняются ли следующие требования:
     * <ul>
     *     <li>Пользователь пристствует в базе данных</li>
     *     <li>Пользователь авторизован</li>
     *     <li>Пользователь не заблокирован</li>
     * </ul>
     * @param userDao интерфейс для CRUD операций к базе данных пользователя
     * @param user текущий пользователь
     * @return <b>true</b>: в случае, если доступ есть, иначе <b>false</b>
     */
    public static boolean hasAccess(UserDao userDao, User user) {
        if (user == null) {
            return false;
        }

        String email = user.getEmail();

        if (userDao.get(email) == null) {
            return false;
        }

        if (!user.isAuthorized()) {
            return false;
        }

        if (user.getRole() == Role.BLOCKED || user.getRole() == Role.UNDEFINED) {
            return false;
        }

        return true;
    }
}