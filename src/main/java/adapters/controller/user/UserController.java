package adapters.controller.user;

import adapters.controller.Constants;
import adapters.controller.Validator;
import adapters.controller.user.validator.UserDeleteValidator;
import adapters.controller.user.validator.UserEditValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.iternal.DataDto;
import common.dto.request.user.UserDeleteDto;
import common.dto.request.user.UserEditDto;
import common.dto.response.ResponseDto;
import common.dto.response.user.UserInformationDto;
import common.dto.response.user.UserInformationDtoMapper;
import common.enumiration.Role;
import core.entity.User;
import core.exceptions.adapters.BadRequestException;
import core.exceptions.adapters.ForbiddenException;
import core.exceptions.usecase.InvalidUserInformationException;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserController extends HttpServlet {
    private UserDao userDao;
    private ObjectMapper objectMapper;
    private Map<String, Validator> validatorMap;

    @Override
    public void init() {
        this.userDao = new JdbcUserDao(new DatabaseUtils());
        this.objectMapper = new ObjectMapper();
        this.validatorMap = new HashMap<>();
        validatorMap.put("userDeleteValidator", new UserDeleteValidator());
        validatorMap.put("userEditValidator", new UserEditValidator());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        User sessionUser = userDao.get(sessionUserId);
        String jsonResponse;

        if (request.getParameter("userId") != null) {
            long requestParameterUserId = Long.parseLong(String.valueOf(request.getParameter("userId")));
            if (sessionUser.getRole().equals(Role.ADMINISTRATOR)) {
                User requiredUser = userDao.get(requestParameterUserId);
                UserInformationDto userInfo = UserInformationDtoMapper.INSTANCE.userToUserInformationDto(requiredUser);
                userInfo.setAmountOfHabits(requiredUser.getHabits().values().size());

                jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                        DataDto.builder()
                                .message(Constants.USER_INFORMATION + " ID: " + requestParameterUserId)
                                .responseData(userInfo)
                                .build()
                ));
            } else {
                try {
                    throw new ForbiddenException();
                } catch (ForbiddenException e) {
                    throw new ServletException(e);
                }
            }
        } else {
            UserInformationDto userInfo = UserInformationDtoMapper.INSTANCE.userToUserInformationDto(sessionUser);
            userInfo.setAmountOfHabits(sessionUser.getHabits().values().size());

            jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message(Constants.USER_INFORMATION)
                            .responseData(userInfo)
                            .build()
            ));
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        out.print(jsonResponse);
        out.flush();
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long userId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        UserEditDto userEditDto = objectMapper.readValue(request.getReader(), UserEditDto.class);
        userEditDto.setSessionUserId(userId);

        try {
            validatorMap.get("userEditValidator").validate(userEditDto);
            userDao.edit(getModifiedUser(userEditDto));
        } catch (ForbiddenException | BadRequestException e) {
            throw new ServletException(e);
        }

        String jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                DataDto.builder()
                        .message(Constants.USER_DATA_CHANGED)
                        .build()
        ));

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        out.print(jsonResponse);
        out.flush();
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long sessionUserId = Long.parseLong(String.valueOf(session.getAttribute("id")));
        User sessionUser = userDao.get(sessionUserId);

        String jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                DataDto.builder()
                        .message(Constants.USER_ACCOUNT_WAS_DELETED_SUCCESSFULLY)
                        .build()
        ));

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        if (sessionUser.getRole().equals(Role.ADMINISTRATOR)) {
            UserDeleteDto userDeleteDto = objectMapper.readValue(request.getReader(), UserDeleteDto.class);
            userDeleteDto.setSessionUserId(sessionUserId);

            try {
                validatorMap.get("userDeleteValidator").validate(userDeleteDto);
                userDao.delete(userDeleteDto.getRequiredUserId());

                out.print(jsonResponse);
                out.flush();
            } catch (ForbiddenException | BadRequestException e) {
                throw new ServletException();
            }
        } else {
            userDao.delete(sessionUserId);
            out.print(jsonResponse);
            out.flush();
            session.invalidate();
        }
    }

    /**
     * Формирует новую сущность {@link User}, рассматривает два случая:
     * <ul>
     *      <li>{@link UserEditDto#getRequiredUserId() requiredUserId} == null, в таком случае, пользователь редактирует
     *           свою учётную запись, и мы получаем сущность пользователя через идентификатор текущей сессии</li>
     *      <li>{@link UserEditDto#getRequiredUserId() requiredUserId} != null, в таком случае, администратор редактирует
     *          учётную запись по указанному идентификатору, для этого получаем сущность пользователя через идентификатор
     *          запроса</li>
     * </ul>
     *
     * @param userEditDto dto, содержащий данные запроса
     */
    private User getModifiedUser(UserEditDto userEditDto) {
        User editingUser;
        if (userEditDto.getRequiredUserId() == null) {
            editingUser = userDao.get(userEditDto.getSessionUserId());
        } else {
            editingUser = userDao.get(userEditDto.getRequiredUserId());
        }

        if (userEditDto.getEmail() != null && !userEditDto.getEmail().isEmpty()) {
            try {
                editingUser.setEmail(userEditDto.getEmail());
            } catch (InvalidUserInformationException e) {
                throw new RuntimeException(e);
            }
        }

        if (userEditDto.getUsername() != null && !userEditDto.getUsername().isEmpty()) {
            try {
                editingUser.setUsername(userEditDto.getUsername());
            } catch (InvalidUserInformationException e) {
                throw new RuntimeException();
            }
        }

        if (userEditDto.getPassword() != null && !userEditDto.getPassword().isEmpty()) {
            try {
                editingUser.setPassword(userEditDto.getPassword());
            } catch (InvalidUserInformationException e) {
                throw new RuntimeException();
            }
        }

        return editingUser;
    }
}