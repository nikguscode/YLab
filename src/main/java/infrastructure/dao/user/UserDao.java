package infrastructure.dao.user;

import core.entity.User;

public interface UserDao {
    void add(User user);
    User get(String email);
    void edit(User user);
    void delete(User user);
}
