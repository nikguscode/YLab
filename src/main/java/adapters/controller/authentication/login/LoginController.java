package adapters.controller.authentication.login;

import adapters.controller.Constants;
import adapters.controller.Validator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.iternal.DataDto;
import common.dto.request.authentication.LoginDto;
import common.dto.response.ResponseDto;
import core.annotations.Loggable;
import infrastructure.DatabaseUtils;
import infrastructure.dao.user.impl.JdbcUserDao;
import infrastructure.dao.user.UserDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usecase.authentication.login.impl.Login;
import usecase.session.SessionCreator;

import java.io.IOException;
import java.io.PrintWriter;

@Loggable
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private usecase.authentication.login.Login login;
    private UserDao userDao;
    private ObjectMapper objectMapper;
    private Validator<LoginDto> validator;

    @Override
    public void init() {
        this.login = new Login(new JdbcUserDao(new DatabaseUtils()));
        this.userDao = new JdbcUserDao(new DatabaseUtils());
        this.objectMapper = new ObjectMapper();
        this.validator = new LoginRequestValidator();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
        String jsonResponse;

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        if (request.getSession(false) != null) {
            handleIfUserAlreadyAuthenticated(out);
            return;
        }

        if (login.isSuccess(loginDto)) {
            new SessionCreator().createSession(request, userDao, loginDto.getEmail());

            jsonResponse = objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message(Constants.USER_SUCCESSFULLY_AUTHENTICATED)
                            .build()
            ));
        } else {
            jsonResponse = objectMapper.writeValueAsString(
                    ResponseDto.builder()
                            .result(Constants.UNAUTHENTICATED)
                            .data(DataDto.builder()
                                    .message(Constants.USER_AUTHENTICATION_DATA_INCORRECT)
                                    .build()
                            ).build()
            );
            response.setStatus(401);
        }

        out.print(jsonResponse);
        out.flush();
    }

    private void handleIfUserAlreadyAuthenticated(PrintWriter out) throws JsonProcessingException {
        String jsonResponse = objectMapper.writeValueAsString(
                ResponseDto.builder()
                        .result(Constants.ALREADY_AUTHENTICATED)
                        .data(DataDto.builder()
                                .message(Constants.USER_ALREADY_AUTHENTICATED)
                                .build()
                        ).build()
        );

        out.print(jsonResponse);
        out.flush();
    }
}