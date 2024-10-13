package infrastructure.dao.user;

import static infrastructure.UserDatabase.*;
import core.entity.User;

/**
 * Реализация обращения к локальной базе данных
 */
public class LocalUserDao implements UserDao {
    @Override
    public void add(User user) {
        database.put(user.getEmail(), user);
    }

    @Override
    public User get(String email) {
        return database.get(email);
    }

    @Override
    public void delete(User user) {
        database.remove(user.getEmail());
    }

    @Override
    public void edit(User user) {
        database.put(user.getEmail(), user);
    }
}