package usecase.session;

import core.entity.User;
import infrastructure.dao.user.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Данный класс предназначен для создания сессии
 */
public class SessionCreator {
    /**
     * Создаёт сессию, если она отсуствует. Сессия хранит только те значения, которые пользователь не может изменить
     * @param request запрос для которого проверяется сессия
     * @param userDao интерфейс для взаимодействия с пользователем в базе данных
     * @param email электронная почта пользователя
     * @return сессию пользователя
     */
    public boolean createSession(HttpServletRequest request, UserDao userDao, String email) {
        if (request.getSession(false) == null) {
            User user = userDao.get(email);
            HttpSession session = request.getSession();

            session.setMaxInactiveInterval(60 * 10);
            session.setAttribute("id", user.getId());
            session.setAttribute("is_authorized", user.isAuthorized());
            session.setAttribute("registration_date", user.getRegistrationDate());
            session.setAttribute("authorization_date", user.getAuthorizationDate());

            return true;
        }

        return false;
    }
}