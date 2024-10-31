package adapters.filters.habit;

import core.ExpiredHabitMarkService;
import core.entity.User;
import infrastructure.DatabaseUtils;
import infrastructure.dao.user.UserDao;
import infrastructure.dao.user.impl.JdbcUserDao;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/users", "/habits", "/habits/list"})
public class MarkFilter implements Filter {
    private final UserDao userDao;

    public MarkFilter() {
        DatabaseUtils databaseUtils = new DatabaseUtils();
        this.userDao = new JdbcUserDao(databaseUtils);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        User sessionUser = userDao.get(sessionUserId);

        ExpiredHabitMarkService.checkAllMarks(sessionUser);
    }
}