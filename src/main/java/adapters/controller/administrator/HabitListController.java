package adapters.controller.administrator;

import common.enumiration.Role;
import core.entity.User;
import core.exceptions.adapters.ForbiddenException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.user.JdbcUserDao;
import infrastructure.dao.user.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/habits/list")
public class HabitListController extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        this.userDao = new JdbcUserDao(new DatabaseUtils());
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        User sessionUser = userDao.get(sessionUserId);

        if (sessionUser.getRole().equals(Role.ADMINISTRATOR)) {

        } else {
            try {
                throw new ForbiddenException();
            } catch (ForbiddenException e) {
                throw new ServletException(e);
            }
        }
    }
}
