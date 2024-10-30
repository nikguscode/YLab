package adapters.controller.administrator.userblock;

import adapters.controller.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.iternal.DataDto;
import common.dto.request.administrator.UserBlockDto;
import common.dto.response.ResponseDto;
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

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/blocks")
public class UserBlockController extends HttpServlet {
    private UserDao userDao;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.userDao = new JdbcUserDao(new DatabaseUtils());
        this.objectMapper = new ObjectMapper();
    }

    // позже реализовать get для просмотра заблокированных юзеров
    // позже сделать обобщённый метод для блокировки/разблокировки

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        UserBlockDto userBlockDto = objectMapper.readValue(request.getReader(), UserBlockDto.class);
        User user = userDao.get(sessionUserId);

        if (user.getRole().equals(Role.ADMINISTRATOR)) {
            long requiredId = userBlockDto.getUserId();
            User requiredUser = userDao.get(requiredId);
            requiredUser.setBlocked(true);
            userDao.edit(requiredUser);

            String responseJson = objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message(Constants.USER_BLOCKED)
                            .build()
            ));

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            out.print(responseJson);
            out.flush();
        } else {
            try {
                throw new ForbiddenException();
            } catch (ForbiddenException e) {
                throw new ServletException(e);
            }
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        UserBlockDto userBlockDto = objectMapper.readValue(request.getReader(), UserBlockDto.class);
        User user = userDao.get(sessionUserId);

        if (user.getRole().equals(Role.ADMINISTRATOR)) {
            long requiredId = userBlockDto.getUserId();
            User requiredUser = userDao.get(requiredId);
            requiredUser.setBlocked(false);
            userDao.edit(requiredUser);

            String responseJson = objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message(Constants.USER_UNBLOCKED)
                            .build()
            ));

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            out.print(responseJson);
            out.flush();
        } else {
            try {
                throw new ForbiddenException();
            } catch (ForbiddenException e) {
                throw new ServletException(e);
            }
        }
    }
}