package adapters.controller.habit;

import adapters.controller.Constants;
import adapters.controller.Validator;
import adapters.controller.habit.validator.HabitDeleteValidator;
import adapters.controller.habit.validator.HabitEditValidator;
import adapters.controller.habit.validator.HabitPostValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.iternal.DataDto;
import common.dto.request.habit.HabitDeleteDto;
import common.dto.request.habit.HabitEditDto;
import common.dto.request.habit.HabitPostDto;
import common.dto.response.ResponseDto;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.adapters.BadRequestException;
import core.exceptions.adapters.ForbiddenException;
import core.exceptions.usecase.InvalidFrequencyConversionException;
import core.exceptions.usecase.InvalidHabitInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.habitmarkhistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.user.JdbcUserDao;
import infrastructure.dao.user.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import usecase.habit.HabitCreator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "HabitServlet", urlPatterns = "/habits")
public class HabitController extends HttpServlet {
    private Map<String, Validator> validators;
    private UserDao userDao;
    private HabitDao habitDao;
    private HabitCreator habitCreator;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.validators = new HashMap<>();
        validators.put("habitDeleteValidator", new HabitDeleteValidator());
        validators.put("habitPostValidator", new HabitPostValidator());
        validators.put("habitEditValidator", new HabitEditValidator());

        DatabaseUtils databaseUtils = new DatabaseUtils();
        this.userDao = new JdbcUserDao(databaseUtils);
        this.habitDao = new JdbcHabitDao(databaseUtils, new JdbcHabitMarkHistoryDao(databaseUtils));
        this.habitCreator = new HabitCreator();
        this.objectMapper = new ObjectMapper();
    }

//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        HttpSession session = request.getSession();
//        String jsonResponse;
//        User user = userDao.get(session.getAttribute("id") instanceof Long ?
//                (long) session.getAttribute("id") : null);
//
//        if (request.getParameter("habitId") != null) {
//            long habitId = Long.parseLong(request.getParameter("habitId"));
//            Habit habit = habitDao.get(habitId);
//
//            if (habit != null && (habit.getUserId() != user.getId() || !user.getRole().equals(Role.ADMINISTRATOR))) {
//                jsonResponse = generateJsonResponseIfRequestForbidden();
//                response.setStatus(200);
//            } else {
//                jsonResponse = generateJsonResponseWithHabitInfoByHabitId(habit);
//                response.setStatus(403);
//            }
//        } else if (request.getParameter("userId") != null && user.getRole().equals(Role.ADMINISTRATOR)) {
//            if (user.getId() != Long.parseLong(request.getParameter("userId")) || !user.getRole().equals(Role.ADMINISTRATOR)) {
//                jsonResponse = generateJsonResponseIfRequestForbidden();
//                response.setStatus(200);
//            } else {
//                jsonResponse = generateJsonResponseWithHabitListInfoByUserId(request);
//                response.setStatus(403);
//            }
//        } else {
//            jsonResponse = generateJsonResponseWithHabitListInfoBySessionId(user);
//            response.setStatus(200);
//        }
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        out.print(jsonResponse);
//        out.flush();
//    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        HabitPostDto habitPostDto = objectMapper.readValue(request.getReader(), HabitPostDto.class);
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        User sessionUser = userDao.get(sessionUserId);

        try {
            validators.get("habitPostValidator").validate(habitPostDto);
            Habit createdHabit = habitCreator.create(sessionUser, habitPostDto);
            habitDao.add(createdHabit);

            String jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message(Constants.HABIT_CREATED)
                            .build()
            ));

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            out.print(jsonResponse);
            out.flush();
        } catch (ForbiddenException | BadRequestException e) {
            throw new ServletException();
        } catch (InvalidFrequencyConversionException | InvalidHabitInformationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        HabitEditDto habitEditDto = objectMapper.readValue(request.getReader(), HabitEditDto.class);

        try {
            habitEditDto.setSessionId(sessionUserId);
            validators.get("habitEditValidator").validate(habitEditDto);
            habitDao.edit(getModifiedHabit(habitEditDto));

            String jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message(Constants.USER_DATA_CHANGED)
                            .build()
            ));

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            out.write(jsonResponse);
            out.flush();
        } catch (ForbiddenException | BadRequestException e) {
            throw new ServletException(e);
        } catch (InvalidHabitInformationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        HabitDeleteDto habitDeleteDto = objectMapper.readValue(request.getReader(), HabitDeleteDto.class);

        try {
            habitDeleteDto.setSessionUserId(sessionUserId);
            validators.get("habitDeleteValidator").validate(habitDeleteDto);
            habitDao.delete(habitDeleteDto.getHabitId());

            String jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message(Constants.HABIT_DELETED)
                            .build()
            ));

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(jsonResponse);
            out.flush();
        } catch (ForbiddenException | BadRequestException e) {
            throw new ServletException(e);
        }
    }

    private Habit getModifiedHabit(HabitEditDto dtoClass) throws ForbiddenException, InvalidHabitInformationException {
        Habit requiredHabit;

        if (dtoClass.getHabitId() != null && dtoClass.getHabitId() != 0) {
            requiredHabit = habitDao.get(dtoClass.getHabitId());
        } else {
            throw new ForbiddenException();
        }

        if (requiredHabit != null && dtoClass.getTitle() != null && !dtoClass.getTitle().isEmpty()) {
            requiredHabit.setTitle(dtoClass.getTitle());
        }

        if (requiredHabit != null && dtoClass.getDescription() != null && !dtoClass.getDescription().isEmpty()) {
            requiredHabit.setDescription(dtoClass.getDescription());
        }

        if (requiredHabit != null && dtoClass.getIsCompleted() != null) {
            requiredHabit.setCompleted(dtoClass.getIsCompleted());
        }

        if (requiredHabit != null && dtoClass.getFrequency() != null) {
            requiredHabit.setFrequency(dtoClass.getFrequency());
        }

        return requiredHabit;
    }

//
//    private String generateJsonResponseWithHabitInfoByHabitId(Habit habit) throws JsonProcessingException {
//        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(
//                ResponseDto.builder()
//                        .data(DataDto.builder()
//                                .message("Привычка с id: " + habit.getId())
//                                .responseData(habit)
//                                .build()
//                        ).build()
//        );
//    }
//
//    private String generateJsonResponseWithHabitListInfoByUserId(HttpServletRequest request) throws JsonProcessingException {
//        User requestedUser = userDao.get(Long.parseLong(request.getParameter("userId")));
//        List<Habit> listOfUserHabits = habitDao.getAll(requestedUser).values().stream().toList();
//
//        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(
//                ResponseDto.builder()
//                        .data(DataDto.builder()
//                                .message(Constants.ALL_HABITS)
//                                .responseData(listOfUserHabits)
//                                .build()
//                        ).build()
//        );
//    }
//
//    private String generateJsonResponseWithHabitListInfoBySessionId(User user) throws JsonProcessingException {
//        List<Habit> listOfUserHabits = habitDao.getAll(user).values().stream().toList();
//
//        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(
//                ResponseDto.builder()
//                        .data(DataDto.builder()
//                                .message(Constants.ALL_HABITS)
//                                .responseData(listOfUserHabits)
//                                .build()
//                        ).build()
//        );
//    }
//
//    private String generateJsonResponseIfRequestForbidden() throws JsonProcessingException {
//        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(
//                ResponseDto.builder()
//                        .result(Constants.FORBIDDEN)
//                        .data(DataDto.builder()
//                                .message(Constants.USER_NOT_AUTHORIZED)
//                                .build()
//                        ).build()
//        );
//    }
}