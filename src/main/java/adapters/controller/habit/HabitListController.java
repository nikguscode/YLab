package adapters.controller.habit;

import adapters.controller.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.iternal.DataDto;
import common.dto.response.ResponseDto;
import common.dto.response.habit.informationdto.HabitInformationDtoMapper;
import common.dto.response.habit.listdto.HabitListDto;
import common.dto.response.habit.listdto.HabitListDtoMapper;
import common.enumiration.Role;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.adapters.ForbiddenException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.habitmarkhistory.HabitMarkHistoryDao;
import infrastructure.dao.habitmarkhistory.JdbcHabitMarkHistoryDao;
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

@WebServlet("/habits/list")
public class HabitListController extends HttpServlet {
    private HabitMarkHistoryDao habitMarkHistoryDao;
    private HabitDao habitDao;
    private UserDao userDao;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        DatabaseUtils databaseUtils = new DatabaseUtils();
        this.habitMarkHistoryDao = new JdbcHabitMarkHistoryDao(databaseUtils);
        this.habitDao = new JdbcHabitDao(databaseUtils, habitMarkHistoryDao);
        this.userDao = new JdbcUserDao(databaseUtils);
        this.objectMapper = new ObjectMapper();
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String jsonResponse;
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        User sessionUser = userDao.get(sessionUserId);

        if (request.getParameter("userId") != null) {
            try {
                if (sessionUser.getRole().equals(Role.ADMINISTRATOR)) {
                    long requiredUserId = Long.parseLong(String.valueOf(request.getParameter("userId")));
                    User requiredUser = userDao.get(requiredUserId);
                    jsonResponse = generateListOfHabitsForRequiredUser(requiredUser);
                } else {
                    throw new ForbiddenException();
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException(); // на Spring будет обрабатываться ServetExceptionHandler
            } catch (ForbiddenException e) {
                throw new ServletException(e);
            }
        } else {
            jsonResponse = generateListOfHabitsForRequiredUser(sessionUser);
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        out.print(jsonResponse);
        out.flush();
    }

    private String generateListOfHabitsForRequiredUser(User requiredUser) throws JsonProcessingException {
        Map<Long, Habit> mapOfUserHabits = habitDao.getAll(requiredUser);
        List<HabitListDto> habits = new ArrayList<>();

        for (Map.Entry<Long, Habit> currentHabitEntry : mapOfUserHabits.entrySet()) {
            habits.add(
                    HabitListDtoMapper.INSTANCE.habitToHabitListDto(currentHabitEntry.getValue())
            );
        }

        return objectMapper.writeValueAsString(new ResponseDto<>(
                DataDto.builder()
                        .message(Constants.LIST_OF_ALL_USER_HABITS)
                        .responseData(habits)
                        .build()
        ));
    }
}