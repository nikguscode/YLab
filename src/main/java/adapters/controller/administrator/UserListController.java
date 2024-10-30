package adapters.controller.administrator;

import adapters.controller.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.iternal.DataDto;
import common.dto.response.ResponseDto;
import common.dto.response.administrator.UserListDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/users/list")
public class UserListController extends HttpServlet {
    private UserDao userDao;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.userDao = new JdbcUserDao(new DatabaseUtils());
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        User sessionUser = userDao.get(sessionUserId);

        if (sessionUser.getRole().equals(Role.ADMINISTRATOR)) {
            List<UserListDto> users = new ArrayList<>();
            Map<String, User> usersWhoWillBeConverted = userDao.getAll();

            for (User currentUser : usersWhoWillBeConverted.values()) {
                users.add(new UserListDto(
                        currentUser.getId(),
                        currentUser.getEmail(),
                        currentUser.getUsername(),
                        currentUser.getPassword(),
                        currentUser.isBlocked()
                ));
            }

            String jsonResponse = objectMapper.writeValueAsString(
                    new ResponseDto<>(
                            DataDto.builder()
                                    .message(Constants.USER_LIST)
                                    .responseData(users)
                                    .build()
                    )
            );

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            out.print(jsonResponse);
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
