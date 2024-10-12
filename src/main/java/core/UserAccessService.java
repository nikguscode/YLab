package core;

import core.entity.User;
import core.enumiration.Role;
import infrastructure.dao.user.UserDao;

public class UserAccessService {
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
